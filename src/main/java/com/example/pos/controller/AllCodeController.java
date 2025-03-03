package com.example.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.ResponseData;
import com.example.pos.model.AllCode;
import com.example.pos.service.AllCodeService;

@RestController
@RequestMapping("/api")
public class AllCodeController {
    @Autowired
    private AllCodeService allCodeService;
    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/all_code")
    public ResponseData<List<AllCode>> getAllCode(@RequestParam String type) {
        return allCodeService.getListAllcode(type);
    }
}
