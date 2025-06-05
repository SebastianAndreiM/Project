package com.example.orderservice.export;

import com.example.orderservice.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.List;

public class OrderExportFactory {
    public static ByteArrayInputStream export(String type, List<Order> orders) throws IOException {
        return switch (type) {
            case "csv" -> exportCSV(orders);
            case "json" -> exportJSON(orders);
            case "xml" -> exportXML(orders);
            case "doc" -> exportDOC(orders);
            default -> throw new IllegalArgumentException("Unknown export type");
        };
    }

    private static ByteArrayInputStream exportCSV(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,userId,storeId,productId,size,color,quantity,unitPrice,orderDate\n");
        for (Order o : orders) {
            sb.append(String.format("%d,%d,%d,%d,%s,%s,%d,%.2f,%s\n",
                    o.getId(), o.getUserId(), o.getStoreId(), o.getProductId(), o.getSize(), o.getColor(),
                    o.getQuantity(), o.getUnitPrice(), o.getOrderDate()));
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    private static ByteArrayInputStream exportJSON(List<Order> orders) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, orders);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportXML(List<Order> orders) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlMapper.writeValue(out, orders);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static ByteArrayInputStream exportDOC(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista comenzi:\n\n");
        for (Order o : orders) {
            sb.append("User ID: ").append(o.getUserId()).append("\n");
            sb.append("Store ID: ").append(o.getStoreId()).append("\n");
            sb.append("Produs ID: ").append(o.getProductId()).append("\n");
            sb.append("Mărime: ").append(o.getSize()).append("\n");
            sb.append("Culoare: ").append(o.getColor()).append("\n");
            sb.append("Cantitate: ").append(o.getQuantity()).append("\n");
            sb.append("Preț: ").append(o.getUnitPrice()).append("\n");
            sb.append("Data: ").append(o.getOrderDate()).append("\n\n");
        }
        return new ByteArrayInputStream(sb.toString().getBytes());
    }
}
