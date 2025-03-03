package com.example.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.model.AllCode;
import com.example.pos.repository.AllCodeRepository;

@Service
public class AllCodeService {
    @Autowired
    AllCodeRepository allCodeRepository;

    public ResponseData<List<AllCode>> getListAllcode(String type) {
        List<AllCode> data = allCodeRepository.findByType(type);
        return data.isEmpty() ? new ResponseData<>(1, "Not found", data)
                : new ResponseData<List<AllCode>>(0, "Success", data);
    }
}
