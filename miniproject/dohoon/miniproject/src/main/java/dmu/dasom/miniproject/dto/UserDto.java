package dmu.dasom.miniproject.dto;

import dmu.dasom.miniproject.domain.UserRole;
import dmu.dasom.miniproject.domain.Users;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String userEmail;
    private String userPassword;
    private UserRole userRole;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static UserDto fromEntity(Users user){
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPassword(user.getUserPassword())
                .userRole(user.getUserRole())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }

    public Users toEntity(){
        return Users.builder()
                .userName(this.userName)
                .userEmail(this.userEmail)
                .userPassword(this.userPassword)
                .userRole(this.userRole)
                .build();
    }
}
