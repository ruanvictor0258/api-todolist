package com.example.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.todolist.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;;import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository; // Chamando o repositorio do Usuario;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // <<< NOVO CÓDIGO COMEÇA AQUI >>>
        // Verifica se a requisição é do tipo OPTIONS (para a verificação de CORS)
        // Se for, apenas continua a cadeia de filtros e retorna, sem validar a autenticação.
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        // <<< NOVO CÓDIGO TERMINA AQUI >>>

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tarefas/")) {
            // Pegar a autenticaçao (email e senha)
            var autorizacao = request.getHeader("Authorization"); // Pega as credencias do login e transforma em um numero estranho;
            var authEncoded = autorizacao.substring("Basic".length()).trim(); // Separa a palavra Basic do numero que remete as credencias;
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);  // Tira a decodicaçao das credencias;
            var authString = new String(authDecode); // Passa as credencias para uma String;
            System.out.println(authString); // Printa as credencias na tela do back-end, dessa forma: username:passoword;
            String[] credenciais = authString.split(":"); // separa as credencias
            String username = credenciais[0]; // Atribuindo a credencial de  username para a String Username
            String password = credenciais[1]; // Atribuindo a credencial de password para a String de Password;
            System.out.println(username); // Printa Username na tela do back-end
            System.out.println(password); // Printa Password na tela do back-end

            // Validar email
            var user = this.userRepository.findByEmail(username); // adicionando o valor do email para a variavel USER
            if (user == null) { // Verificando se USER é igual a nulo, porque ser for, ele tem que logar em um usuario valido, ai vai dar o erro 41;
                response.sendError(401); // ERRO SE O USUARIO FOR NULL ( QUER DIZER QUE O USUARIO AINDA NÃO FOI CADASTRADO!)
            } else {
                // Validar senha
                var passowordVeri = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passowordVeri.verified) {
                    // segue viagem
                    request.setAttribute("usuarioId", user.getId());
                    filterChain.doFilter(request, response); // Request é tudo o que esta vindo da requisizão e reponse é o que a gente esta enviando para o nosso usuario..
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
