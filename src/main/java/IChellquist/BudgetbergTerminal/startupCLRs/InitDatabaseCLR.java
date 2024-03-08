package IChellquist.BudgetbergTerminal.startupCLRs;

import IChellquist.BudgetbergTerminal.models.ERole;
import IChellquist.BudgetbergTerminal.models.Role;
import IChellquist.BudgetbergTerminal.models.User;
import IChellquist.BudgetbergTerminal.repository.RoleRepository;
import IChellquist.BudgetbergTerminal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitDatabaseCLR implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${default_user}")
    private String defaultUser;

    @Value("${default_password}")
    private String defaultPassword;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    @Order(1)
    public void run(String... args){

        if (!mongoTemplate.collectionExists("roles")) mongoTemplate.createCollection("roles");
        if (!mongoTemplate.collectionExists("users")) mongoTemplate.createCollection("users");
        if (!mongoTemplate.collectionExists("StockReports")) mongoTemplate.createCollection("StockReports");

        Role admin = Role.builder().name(ERole.ROLE_ADMIN).build();
        Role user = Role.builder().name(ERole.ROLE_USER).build();
        roleRepository.save(admin);
        roleRepository.save(user);

        if (!userRepository.existsByUsername(defaultUser)){
            User newUser = User.builder()
                    .username(defaultUser)
                    .password(passwordEncoder.encode(defaultPassword))
                    .roles(new HashSet<>(Arrays.asList(roleRepository.findByName(ERole.ROLE_ADMIN).get())))
                    .build();
            userRepository.save(newUser);
        }
    }
}
