package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/default")
public interface cuentaController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta creada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "500", description = "Error el crear la cuenta")
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<Account> crearCuenta(@RequestBody Account account);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe una o varias cuenta para el cliente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existen cuentas para el cliente")
    })
    @GetMapping ()
    ResponseEntity <Collection<Account>> obtenerCuentas(@RequestParam("customerId") Long customerId);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existe cuenta para el cliente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existe cuenta para el cliente"),
            @ApiResponse(responseCode = "409", description = "La cuenta no pertenece al owner indicado")
    })
    @GetMapping (value = "/{accountId}/{ownerId}")
    ResponseEntity <Account> entregarCuenta(@PathVariable("accountId") Long accountId, @PathVariable("ownerId") Long ownerId);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta informada a eliminar")
    })
    @DeleteMapping (value = "/{accountId}")
    ResponseEntity eliminarCuenta(@PathVariable("accountId") Long accountId);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta modificada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta informada a modificar"),
            @ApiResponse(responseCode = "500", description = "Error modificando la cuenta")
    })
    @PutMapping (value = "/{accountId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity <Account> modificarCuenta(@PathVariable("accountId") Long accountId, @RequestBody Account account);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta modificada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta informada a eliminar"),
            @ApiResponse(responseCode = "409", description = "La cuenta no pertenece al owner indicado")
    })
    @DeleteMapping (value = "/{accountId}/{customerId}")
    ResponseEntity eliminarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta modificada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "404", description = "No existe la cuenta informada a modificar"),
            @ApiResponse(responseCode = "409", description = "La cuenta no pertenece al owner indicado"),
            @ApiResponse(responseCode = "500", description = "Error modificando la cuenta")
    })
    @PutMapping (value = "/{accountId}/{customerId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity <Account> modificarCuentaCliente(@PathVariable("accountId") Long accountId, @PathVariable("customerId") Long customerId, @RequestBody Account account);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuentas eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request")
    })
    @DeleteMapping (value = "/{customerId}/all")
    ResponseEntity eliminarCuentasCliente(@PathVariable("customerId") Long customerId);


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El prestamos es valido (no supera el 80% del dinero en cuentas)"),
            @ApiResponse(responseCode = "400", description = "No recibimos respuesta del request"),
            @ApiResponse(responseCode = "409", description = "El prestamos no es valido (supera el 80% del dinero en cuentas)")
    })
    @GetMapping (value = "/{customerId}/validar")
    ResponseEntity validarPrestamo(@PathVariable("customerId") Long customerId, @RequestParam("cantidadSolicitada") Double cantidadSolicitada);

}
