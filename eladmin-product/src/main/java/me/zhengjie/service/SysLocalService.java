package me.zhengjie.service;

import me.zhengjie.service.dto.SysLocalDto;

import java.util.List;
import java.util.Set;

public interface SysLocalService {

  List<SysLocalDto> getLocal();

  public Set<String> getLocalByuser (final long id);

}
