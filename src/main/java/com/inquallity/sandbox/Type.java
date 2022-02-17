package com.inquallity.sandbox;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

enum Type {
    F(255), S(120), T(450), FO(123);
    private final int id;

    Type(int id) {
        this.id = id;
    }

    @Nullable
    public static Type fromList(@NotNull List<Obj> list) {
        return list.stream()
                .map(Type::fromId)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static Type fromList2(@NotNull List<Obj> list) {
        for (Obj o : list) {
            final Type t = fromId(o);
            if (t != null) return t;
        }
        return null;
    }

    @Nullable
    public static Type fromId(@NotNull Obj obj) {
        for (Type t : values()) {
            if (t.id == obj.type) return t;
        }
        return null;
    }
}