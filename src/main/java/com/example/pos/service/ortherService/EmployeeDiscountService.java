package com.example.pos.service.ortherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.employeeDiscount.CreateDiscountDTO;
import com.example.pos.dto.employeeDiscount.UpdateDiscount;
import com.example.pos.model.EmployeeDiscount;
import com.example.pos.repository.EmployeeDiscountRepository;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class EmployeeDiscountService {
    @Autowired
    EmployeeDiscountRepository employeeDiscountRepository;

    public ResponseData<EmployeeDiscount> createEmployeeDiscount(CreateDiscountDTO dto) {
        try {
            if (dto.getName() == null || dto.getDiscountValue() == 0) {
                return new ResponseData<EmployeeDiscount>(2, "Missing required field", (EmployeeDiscount) null);
            }
            EmployeeDiscount data = employeeDiscountRepository.findByName(dto.getName());
            if (data != null) {
                return new ResponseData<EmployeeDiscount>(3, "Employee name already exists", (EmployeeDiscount)null);
            }
            String code = RandomStringUtils.randomAlphanumeric(16);
            double discount = dto.getDiscountValue() / 100;
            EmployeeDiscount employeeDiscount = new EmployeeDiscount(null, dto.getName(),
                    code,
                    discount, true);

            return new ResponseData<EmployeeDiscount>(0, "Success", employeeDiscountRepository.save(employeeDiscount));
        } catch (Exception e) {
            return new ResponseData<EmployeeDiscount>(1, "Error: " + e.getMessage(), (EmployeeDiscount)null);
        }
    }

    public ResponseData<EmployeeDiscount> updateEmployeeDiscountService(UpdateDiscount dto) {
        try {
            if (dto.getId() == null) {
                return new ResponseData<EmployeeDiscount>(3, "Missing required field", (EmployeeDiscount)null);
            }
            boolean checkData = employeeDiscountRepository.existsById(dto.getId());
            if (checkData == true) {
                EmployeeDiscount employeeDiscount = employeeDiscountRepository.findById(dto.getId()).orElse(null);
                employeeDiscount.setName(dto.getName());
                employeeDiscount.setDiscountValue(dto.getDiscountValue() / 100);
                employeeDiscount.setActive(dto.getActive());
                EmployeeDiscount res = employeeDiscountRepository.save(employeeDiscount);
                return new ResponseData<EmployeeDiscount>(0, "Success", res);
            } else {
                return new ResponseData<EmployeeDiscount>(2, "Employee not found", (EmployeeDiscount)null);
            }
        } catch (Exception e) {
            return new ResponseData<EmployeeDiscount>(1, "Error: " + e.getMessage(), (EmployeeDiscount) null);
        }
    }

    public ResponseData<EmployeeDiscount> deleteEmployeeDiscountService(Long id) {
        try {
            if (id == null) {
                return new ResponseData<EmployeeDiscount>(3, "Missing required field", (EmployeeDiscount)null);
            }
            boolean checkData = employeeDiscountRepository.existsById(id);
            if (checkData == true) {
                employeeDiscountRepository.deleteById(id);
                return new ResponseData<EmployeeDiscount>(0, "Success", (EmployeeDiscount)null);
            } else {
                return new ResponseData<EmployeeDiscount>(2, "Employee not found", (EmployeeDiscount)null);
            }
        } catch (Exception e) {
            return new ResponseData<EmployeeDiscount>(1, "Error: " + e.getMessage(), (EmployeeDiscount)null);
        }
    }

    public ResponseData<Page<EmployeeDiscount>> getListDiscountService(int page, int size, String sortBy,
            String direction, String search) {
        try {
            Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(Math.max(-1, 0), size, sort);
            Page<EmployeeDiscount> discountPage;
            if (search != null && !search.trim().isEmpty()) {
                discountPage = employeeDiscountRepository.findByNameContainingIgnoreCase(search, pageable);
            } else {
                discountPage = employeeDiscountRepository.findAll(pageable);
            }

            return new ResponseData<Page<EmployeeDiscount>>(0, "success", discountPage);
        } catch (Exception e) {
            return new ResponseData<Page<EmployeeDiscount>>(1, "Error from server" + e.getMessage(),(Page<EmployeeDiscount>) null);
        }
    }
}
