package com.example.demo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.JwtTokenPayload;
import com.example.demo.security.JwtTokenProvider;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, Object> template;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		String tokenWithoutBearer = null;
		if (token != null) {
			tokenWithoutBearer = token.replace("Bearer ", "");
		}
		if (tokenWithoutBearer != null) {
			boolean check = template.opsForValue().get(tokenWithoutBearer) == null;
			if (!check) {
				SecurityContextHolder.getContext().setAuthentication(null);
			} else {
				JwtTokenPayload payload = jwtTokenProvider.extractJwtPayload(tokenWithoutBearer);
				List<String> authorities = new ArrayList<>();
				authorities.add(payload.getRole());
				authorities = Stream.concat(authorities.stream(), payload.getAuthorities().stream())
						.collect(Collectors.toList());
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(payload,
						null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));

			}
		}
		filterChain.doFilter(request, response);
	}

}
