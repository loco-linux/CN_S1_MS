package com.example.tienda.model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;

@Data
@Builder
public class Asset {
     String name;
     String key;
     String url; // URL url;
}
