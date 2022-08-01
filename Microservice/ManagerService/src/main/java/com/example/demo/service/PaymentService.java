package com.example.demo.service;

import com.example.demo.client.OrderFeignClient;
import com.example.demo.client.StoreFeignClient;
import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.utils.report.ExcelUtil;
import com.example.demo.utils.report.PdfUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.Constants.*;

@Service
public class PaymentService {
    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * Function to save payment
     *
     * @param payment
     * @return payment
     */
    public Payment saveAndUpdatePaymentOfStore(Payment payment) {
        String token = managerService.generateToken();
        OrderDTO orderDTO = orderFeignClient.getOrderById(payment.getOrderId(), token).getBody();
        if (orderDTO == null) {
            return null;
        } else {
            StoreDTO storeDTO = storeFeignClient.getStoreById(orderDTO.getStoreId(), token).getBody();
            Payment paymentRepo = paymentRepository.getMaxPaymentByOrderId(orderDTO.getId());
            if (paymentRepo == null) {
                if (payment.getMoneyPaid() > orderDTO.getTotalPrice()) {
                    return null;
                }
                payment.setTotalMoney(orderDTO.getTotalPrice());
                payment.setAccumulatedMoney(payment.getMoneyPaid());
                payment.setMoneyUnpaid(orderDTO.getTotalPrice() - payment.getMoneyPaid());
                payment.setStoreUser(storeDTO.getUserName());
                if (payment.getMoneyUnpaid() == 0) {
                    payment.setStatus("Completed");
                } else {
                    payment.setStatus("Incomplete");
                }
                payment.setPaymentDate(LocalDateTime.now());
                return paymentRepository.save(payment);
            }
            if (paymentRepo.getMoneyUnpaid() > 0) {
                if (payment.getMoneyPaid() > paymentRepo.getMoneyUnpaid()) {
                    return null;
                }
                Payment payment1 = new Payment();
                payment1.setTotalMoney(paymentRepo.getTotalMoney());
                payment1.setAccumulatedMoney(paymentRepo.getAccumulatedMoney() + payment.getMoneyPaid());
                payment1.setMoneyPaid(payment.getMoneyPaid());
                payment1.setMoneyUnpaid(paymentRepo.getMoneyUnpaid() - payment.getMoneyPaid());
                if (payment1.getMoneyUnpaid() == 0) {
                    payment1.setStatus("Completed");
                } else {
                    payment1.setStatus("Incomplete");
                }
                payment1.setStoreUser(storeDTO.getUserName());
                payment1.setOrderId(orderDTO.getId());
                payment1.setPaymentDate(LocalDateTime.now());
                return paymentRepository.save(payment1);
            } else {
                return null;
            }
        }
    }

    /**
     * Function to get All Payment
     *
     * @return list of payments
     */
    public List<Payment> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments == null) {
            return null;
        }
        return payments;
    }

    /**
     * Function to get payment by id
     *
     * @param id
     * @return payment
     */
    public Payment getPaymentById(long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            return payment.get();
        }
        return null;
    }

    /**
     * Function to get payments by order id
     *
     * @param orderId
     * @return list of payments
     */
    public List<Payment> getAllPaymentByOrderId(long orderId) {
        List<Payment> payments = paymentRepository.findPaymentByOrderId(orderId);
        if (payments == null) {
            return null;
        }
        return payments;
    }

    /**
     * Function to delete payment
     *
     * @param id
     * @return true or false
     */
    public boolean deletePayment(long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Function to export PDF Report.
     *
     * @param response
     * @param startDate
     * @param endDate
     * @throws IOException
     */
    public void getPaymentToReportPDF(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        LocalTime localTime = LocalTime.of(HOUR, MINUTE, SECOND);
        LocalDateTime startDateTime = startDate.atTime(localTime);
        LocalDateTime endDateTime = endDate.atTime(localTime).plusDays(1);
        List<Payment> payments = paymentRepository.getAllPaymentByDateTime(startDateTime, endDateTime);
        PdfUtil pdfUtil = new PdfUtil();
        pdfUtil.generatePdfToReport(response, payments, startDate, endDate);
    }

    /**
     * Function to export Excel Report
     *
     * @param response
     * @param startDate
     * @param endDate
     * @throws IOException
     */
    public void getPaymentToReportExcel(HttpServletResponse response, LocalDate startDate, LocalDate endDate) throws IOException {
        LocalTime localTime = LocalTime.of(HOUR, MINUTE, SECOND);
        LocalDateTime startDateTime = startDate.atTime(localTime);
        LocalDateTime endDateTime = endDate.atTime(localTime).plusDays(1);
        List<Payment> payments = paymentRepository.getAllPaymentByDateTime(startDateTime, endDateTime);
        ExcelUtil excelUtil = new ExcelUtil();
        excelUtil.generateExcel(response, payments, startDate, endDate);
    }


//    @Scheduled(cron = "*/20 * * * * *")
//    public void getPaymentToReport(LocalDateTime startDate, LocalDateTime endDate){
////        LocalDateTime localDateTime = LocalDateTime.parse("2022-07-29T18:49:30");
////        LocalDateTime localDateTime1 =LocalDateTime.parse("2022-07-29T20:00:00").plusDays(1);
//        LocalDate  localDate = LocalDate.now();
//        LocalTime localTime = LocalTime.now();
//        LocalDateTime localDateTime2 = localDate.atTime(localTime);
//        LocalDateTime localDateTime = localDateTime2.plusDays(1);
////        List<Payment> payments = paymentRepository.getAllPaymentByDateTime(localDateTime, localDateTime1);
////        System.out.println(payments.get(0).getId());
//        System.out.println(localDateTime2);
//        System.out.println(localDateTime);
//
//    }

}
