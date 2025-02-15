package com.assignment.user.services.serviceImpl;

import com.assignment.user.entities.User;
import com.assignment.user.request.LoginRequest;
import com.assignment.user.response.JwtResponse;
import com.assignment.user.response.Response;
import com.assignment.user.response.UserResponseDto;
import com.assignment.user.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.assignment.user.repositories.UserRepository;
import com.assignment.user.util.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get authenticated user details
            User user = userRepository.findByEmail(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate JWT Token
            String token = jwtTokenProvider.generateToken(user);

            // Return JWT token
            return ResponseEntity.ok(new JwtResponse(token,"Login successful"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, "Invalid username or password"));
        }

    }

    @Override
    public Response getUserInfo() throws BadRequestException {
       String email = jwtTokenProvider.getEmailFromContext();
       if(email!=null) {
        Optional<User> user =  userRepository.findByEmail(email);
        if(user.isPresent()){
            Response response = new Response();
            UserResponseDto userResponseDto = UserResponseDto.getResponseDto(user.get());
            response.setData(userResponseDto);
            response.setSuccess(true);
            response.setMessage("User Found");
            return response;
        }
       }
       else throw new BadRequestException("User doesn't exists");
       return null;
    }
}
