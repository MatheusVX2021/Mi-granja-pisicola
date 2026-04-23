package com.example.myapplication.data.local.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class GastoWithCategory {
    @Embedded
    public Gasto gasto;

    @Relation(
            parentColumn = "idCatGasto",
            entityColumn = "id"
    )
    public CatGasto category;
}
