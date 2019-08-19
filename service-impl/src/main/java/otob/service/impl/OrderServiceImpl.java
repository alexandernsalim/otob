package otob.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import otob.model.constant.Status;
import otob.model.entity.CartItem;
import otob.model.entity.Order;
import otob.model.entity.Product;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.model.filter.ExportFilter;
import otob.model.filter.OrderFilter;
import otob.repository.OrderRepository;
import otob.service.OrderService;
import otob.service.ProductService;
import otob.service.UserService;
import otob.util.mapper.BeanMapper;
import otob.web.model.OrderDto;
import otob.web.model.PageableOrderDto;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public PageableOrderDto getAllOrder(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findAll(pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        if(!orderRepository.existsByOrdId(orderId)){
            throw new CustomException(
                ErrorCode.ORDER_NOT_FOUND.getCode(),
                ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }

        return orderRepository.findByOrdId(orderId);
    }

    @Override
    public PageableOrderDto getAllOrderByUserEmail(String userEmail, Integer page, Integer size) {
        if (!userService.checkUser(userEmail)) {
            throw new CustomException(
                    ErrorCode.USER_NOT_FOUND.getCode(),
                    ErrorCode.USER_NOT_FOUND.getMessage()
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findAllByUserEmail(userEmail, pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public PageableOrderDto getAllOrderByFilter(String date, String status, Integer page, Integer size) {
        OrderFilter filter = OrderFilter.builder()
                .orderDate((date == null) ? "" : date)
                .orderStatus((status == null) ? "" : status)
                .build();

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pages = orderRepository.findOrderWithFilter(filter, pageable);
        List<Order> orders = pages.getContent();

        return generateResult(pages, orders);
    }

    @Override
    public Order acceptOrder(String ordId) {
        if (!orderRepository.existsByOrdId(ordId)) {
            throw new CustomException(
                    ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrdId(ordId);
        order.setOrdStatus(Status.ORD_ACCEPT);

        return orderRepository.save(order);
    }

    @Override
    public Order rejectOrder(String ordId) {
        if (!orderRepository.existsByOrdId(ordId)) {
            throw new CustomException(
                    ErrorCode.ORDER_NOT_FOUND.getCode(),
                    ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }

        Order order = orderRepository.findByOrdId(ordId);

        if (!order.getOrdStatus().equals(Status.ORD_WAIT)) {
            throw new CustomException(
                    ErrorCode.ORDER_PROCESSED.getCode(),
                    ErrorCode.ORDER_PROCESSED.getMessage()
            );
        }

        order.setOrdStatus(Status.ORD_REJECT);

        List<CartItem> ordItems = order.getOrdItems();

        for (CartItem item : ordItems) {
            Product product = productService.getProductById(item.getProductId());
            int currStock = product.getStock();

            product.setStock(currStock + item.getQty());

            productService.updateProductById(product.getProductId(), product);
        }

        return orderRepository.save(order);
    }

    @Override
    public ByteArrayInputStream exportOrder(String year, String month) {
        Date date = new Date();
        String defaultYear = new SimpleDateFormat("yyyy").format(date);

        ExportFilter filter = ExportFilter.builder()
                .month((month == null) ? "" : month)
                .year((year == null) ? defaultYear : year)
                .build();

        List<Order> orders = orderRepository.findOrderWithFilter(filter);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            List<String> headerName = new ArrayList<>(Arrays.asList(
                "Order ID", "Customer", "Order Date", "Items", "Total Item", "Total Price", "Status"
            ));

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Order History");
            Row header = sheet.createRow(0);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal());

            for(int i = 0; i < headerName.size(); i++) {
                Cell headerCell = header.createCell(i);
                headerCell.setCellValue(headerName.get(i));
                headerCell.setCellStyle(headerStyle);
            }

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);

            for(int i = 1; i <= orders.size(); i++) {
                Row row = sheet.createRow(i);
                Order order = orders.get(i-1);
                StringBuilder items = new StringBuilder();
                for (int j = 0; j < order.getTotItem(); j++) {
                    items.append(j+1)
                        .append(". ")
                        .append(order.getOrdItems().get(j).getName())
                        .append(System.lineSeparator());
                }

                row.createCell(0).setCellValue(order.getOrdId());
                row.createCell(1).setCellValue(order.getUserEmail());
                row.createCell(2).setCellValue(order.getOrdDate());
                row.createCell(3).setCellValue(items.toString());
                row.createCell(4).setCellValue(order.getTotItem());
                row.createCell(5).setCellValue(order.getTotPrice());
                row.createCell(6).setCellValue(order.getOrdStatus());
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            logger.warn("Error creating excel file");

            throw new CustomException(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
            );
        }
    }

    private void checkEmptiness(List<Order> orders) throws CustomException {
        if(orders.isEmpty()) {
            throw new CustomException(
                ErrorCode.ORDER_NOT_FOUND.getCode(),
                ErrorCode.ORDER_NOT_FOUND.getMessage()
            );
        }
    }

    private PageableOrderDto generateResult(Page<Order> pages, List<Order> orders) {
        checkEmptiness(orders);

        List<OrderDto> ordersResult = BeanMapper.mapAsList(orders, OrderDto.class);
        PageableOrderDto result = PageableOrderDto.builder()
                .totalPage(pages.getTotalPages())
                .orders(ordersResult)
                .build();

        return result;
    }

}
