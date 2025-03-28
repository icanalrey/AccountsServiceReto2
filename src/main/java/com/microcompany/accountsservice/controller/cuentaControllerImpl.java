package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.enums.TipoCuenta;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Collection;

@RestController
@RequestMapping("/account")
public class cuentaControllerImpl implements cuentaController{

    @Autowired
    private IAccountService accountService;

    @Override
    public ResponseEntity<Account> crearCuenta(Account account) {

        if(account == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (!TipoCuenta.PERSONAL.toString().equals(account.getType()) && !TipoCuenta.COMPANY.toString().equals(account.getType())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Account nuevaCuenta = accountService.create(account);

        return nuevaCuenta != null && nuevaCuenta.getId()>0 ? ResponseEntity.ok().body(account): ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    @Override
    public ResponseEntity<Collection<Account>> obtenerCuentas(Long customerId) {

        if(customerId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Account> listaCuentas = accountService.getAccountByOwnerId(customerId);
        return !CollectionUtils.isEmpty(listaCuentas) ? ResponseEntity.ok().body(listaCuentas) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Account> entregarCuenta(Long accountId, Long ownerId) {

        if(accountId == null || ownerId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Account cuenta = accountService.getAccount(accountId);

        if(cuenta == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(!ownerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok().body(cuenta);
    }

    @Override
    public ResponseEntity eliminarCuenta(Long accountId) {

        if (accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        accountService.delete(accountId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Account> modificarCuenta(Long accountId, Account account) {

        if (account == null || accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (account.getType() != null && !TipoCuenta.PERSONAL.toString().equals(account.getType()) && !TipoCuenta.COMPANY.toString().equals(account.getType())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cuenta = accountService.updateAccount(accountId, account);

        return cuenta != null ? ResponseEntity.ok().body(cuenta) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity eliminarCuentaCliente(Long accountId, Long customerId) {

        if (customerId == null || accountId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!customerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La cuenta que desea eliminar no pertenece al cliente indicado");
        }

        accountService.delete(accountId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Account> modificarCuentaCliente(Long accountId, Long customerId, Account account) {

        if (customerId == null || accountId == null || account == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        if (account.getOwnerId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (account.getType() != null && !TipoCuenta.PERSONAL.toString().equals(account.getType()) && !TipoCuenta.COMPANY.toString().equals(account.getType())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Account cuenta = accountService.getAccount(accountId);

        if (cuenta == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!customerId.equals(cuenta.getOwnerId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        cuenta = accountService.updateAccount(accountId, account);

        return (cuenta != null) ? ResponseEntity.ok().body(cuenta) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity eliminarCuentasCliente(Long customerId) {

        if (customerId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        accountService.deleteAccountsUsingOwnerId(customerId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity validarPrestamo(Long customerId, Double cantidadSolicitada) {

        if (customerId == null || cantidadSolicitada == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        double maximoPermitido = accountService.getAccountByOwnerId(customerId).stream().map(Account::getBalance).reduce(0D, Double::sum) * 0.8D;

        if (maximoPermitido >= cantidadSolicitada){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("El prestamo no puede ser concedido porque la cantidad solicitada supera al 80% de la cantidad total disponible del cliente");
    }
}
