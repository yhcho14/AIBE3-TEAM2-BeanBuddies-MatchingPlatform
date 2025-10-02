package com.back.domain.client.client.controller;

import com.back.domain.client.client.dto.ClientUpdateForm;
import com.back.domain.client.client.dto.ClientUpdateResponse;
import com.back.domain.client.client.entity.Client;
import com.back.domain.client.client.service.ClientService;
import com.back.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    @PutMapping
    public ApiResponse<ClientUpdateResponse> updateClient(ClientUpdateForm info) {
        Client client = clientService.updateClient(
                info.id(),
                info.companySize(),
                info.companyDescription(),
                info.representative(),
                info.businessNo(),
                info.companyPhone(),
                info.companyEmail()
        );

        ClientUpdateResponse response = new ClientUpdateResponse(client);

        return new ApiResponse<>(
                "204",
                "클라이언트 정보 변경",
                response
        );
    }
}
