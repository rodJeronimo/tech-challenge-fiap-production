package com.fiap.techchallenge.production.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.techchallenge.production.application.useCases.OrderUseCases;
import com.fiap.techchallenge.production.presentation.dtos.ErrorResponseDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderDto;
import com.fiap.techchallenge.production.presentation.dtos.OrderUpdateStatusRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/production")
@Tag(name = "Produção", description = "Operações para gerenciamento de pedidos para produção")
@RequiredArgsConstructor
public class ProductionController {

    private final OrderUseCases orderUseCases;

    @Operation(summary = "Atualizar status da ordem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrderDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para a atualização do status", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Problemas internos durante a a atualização do status", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))
            })
    })
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderDto> updateOrderStatus(
            @RequestBody OrderUpdateStatusRequestDto updateStatusRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(orderUseCases.updateStatus(updateStatusRequest));
    }
}
