package br.com.devmaicon.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.devmaicon.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();
        System.out.println(servletPath);

        // se for rota de user, ele não precisa de autenticação
        if (servletPath.contains("/user")) {
            filterChain.doFilter(request, response);
            return;
        }

        // String pathController = request.getRequestURI();
        // if (pathController.contains("/user")) {
        // filterChain.doFilter(request, response);
        // return;
        // }

        try {
            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.split(" ")[1];

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecoded);

            String[] credentials = authString.split(":");
            var username = credentials[0];
            var password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

            if (!passwordVerify.verified) {
                throw new RuntimeException("Invalid password");
            }

            request.setAttribute("idUser", user.getId());
            filterChain.doFilter(request, response);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }

}
