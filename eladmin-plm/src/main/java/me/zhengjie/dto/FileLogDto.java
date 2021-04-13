package me.zhengjie.dto;

import lombok.AllArgsConstructor;
import me.zhengjie.request.Sql;

@AllArgsConstructor
public class FileLogDto extends Sql {
    public String fileName;
    public String people;
    public String opTime;
    public String opType;

    public FileLogDto() {
    }
}
