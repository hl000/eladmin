package me.zhengjie.domain;

import lombok.Data;
import me.zhengjie.service.dto.AdmspeechheadDto;

import java.util.List;

@Data
public class ProductResult {

  private boolean isSaveProduct;

  private List<AdmspeechheadDto> admspeechheadList;
}
