package com.nyfaria.parkbuilder.entity.ai.iface;

import com.nyfaria.parkbuilder.entity.ai.nums.Gender;

public interface IHasGender {
    String getVariantName();
    void setVariant(Gender variant);
}
