package org.example.util;

import org.example.controller.UserControllerImpl;
import org.example.model.dto.UserResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserResponseAssembler implements RepresentationModelAssembler<UserResponseDTO, EntityModel<UserResponseDTO>> {
    private final static String BASE_URL ="http://localhost:8080/api/users";

    @Override
    public EntityModel<UserResponseDTO> toModel(UserResponseDTO user) {
        try {
            return EntityModel.of(user,
                linkTo(UserControllerImpl.class)
                    .slash(user.id())
                    .withSelfRel(),

                linkTo(UserControllerImpl.class)
                    .slash("email")
                    .slash(user.email())
                    .withRel("email"),

                linkTo(UserControllerImpl.class)
                    .withRel("users"),

                Link.of(linkTo(UserControllerImpl.class).toString() + "/" + user.id(), "delete")
            );
        } catch (Exception e) {
            return EntityModel.of(user,
                Link.of(BASE_URL + "/" + user.id(), "self"),
                Link.of(BASE_URL + "/email/" + user.email(), "email"),
                Link.of(BASE_URL, "users")
            );
        }
    }

    public EntityModel<UserResponseDTO> toModelWithEmailLink(UserResponseDTO user) {
        try {
            return EntityModel.of(user,
                linkTo(UserControllerImpl.class)
                    .slash("email")
                    .slash(user.email())
                    .withSelfRel(),

                linkTo(UserControllerImpl.class)
                    .slash(user.id())
                    .withRel("user"),

                linkTo(UserControllerImpl.class)
                    .withRel("users")
            );
        } catch (Exception e) {
            String baseUrl = "http://localhost:8080/api/users";
            return EntityModel.of(user,
                Link.of(BASE_URL + "/email/" + user.email(), "self"),
                Link.of(BASE_URL + "/" + user.id(), "user"),
                Link.of(BASE_URL, "users")
            );
        }
    }
}
