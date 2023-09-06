package com.workintech.security.service;

import com.workintech.security.entity.Member;
import com.workintech.security.entity.Role;
import com.workintech.security.repository.MemberRepository;
import com.workintech.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthenticationService {//register işlemleri
    private MemberRepository memberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member register(String email, String password){

        Optional<Member> foundMember = memberRepository.findMemberByEmail(email);
        if(foundMember.isPresent()){
            //throw Exception
            return null;
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role memberRole = roleRepository.findByAuthority("USER").get();//yeni registerlar hep USER rollü, admin databeseden manuel olarak yapılır, yani sqlde tabloya gidip rol no değişir
        Set<Role> roles = new HashSet<>();//birden fazla rolü olabilir diye
        roles.add(memberRole);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setAuthorities(roles);
        return memberRepository.save(member);
    }
}
}
