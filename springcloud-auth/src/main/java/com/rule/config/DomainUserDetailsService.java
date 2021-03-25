package com.rule.config;

import lombok.extern.slf4j.Slf4j;

//@Service("userDetailsService") implements UserDetailsService
@Slf4j
public class DomainUserDetailsService {

//    @Resource
//    private UserMapper userMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 数据库中查询用户信息
////        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
////        wrapper.eq("username", username);
////        UserInfo user = userMapper.selectOne(wrapper);
////        return new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList("admin", "manager"));
////
//        if (!"codesheep".equals(username)) {
//            throw new UsernameNotFoundException("用户" + username + "不存在");
//        }
//        return new User(username,
//                passwordEncoder.encode("123456"),
//                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_NORMAL,ROLE_MEDIUM"));
//    }
}

