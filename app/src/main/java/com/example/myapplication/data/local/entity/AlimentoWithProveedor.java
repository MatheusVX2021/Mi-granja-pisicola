package com.example.myapplication.data.local.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class AlimentoWithProveedor {
    @Embedded
    public Alimento alimento;

    @Relation(
            parentColumn = "idProveedor",
            entityColumn = "id"
    )
    public Proveedor proveedor;
}
