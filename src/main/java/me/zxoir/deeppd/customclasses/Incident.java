package me.zxoir.deeppd.customclasses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Incident {
    UUID uuid;
    Date date;
}
