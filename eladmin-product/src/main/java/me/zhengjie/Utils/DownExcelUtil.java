/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.Utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

public class DownExcelUtil extends cn.hutool.core.io.FileUtil {

  private static final Logger log = LoggerFactory.getLogger(DownExcelUtil.class);

  /**
   * 系统临时目录
   * <br>
   * windows 包含路径分割符，但Linux 不包含,
   * 在windows \\==\ 前提下，
   * 为安全起见 同意拼装 路径分割符，
   * <pre>
   *       java.io.tmpdir
   *       windows : C:\Users/xxx\AppData\Local\Temp\
   *       linux: /temp
   * </pre>
   */
  public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

  /**
   * 导出excel
   */
  public static void downloadExcel(List<Map<String, Object>> list, List<Map<String, Object>> layoutList, List<Integer> cellList, HttpServletResponse response) throws IOException {
    String tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
    File file = new File(tempPath);
    BigExcelWriter writer = ExcelUtil.getBigWriter(file);
    layout(writer, layoutList);
    // 一次性写出内容，使用默认样式，强制输出标题
    writer.write(list, true);
    SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
    if (list.size() > 0) {
      sheet.setColumnWidth(0, 10 * 256);
      sheet.setColumnWidth(1, 10 * 256);
      sheet.setColumnWidth(2, 8 * 256);
      sheet.setColumnWidth(3, 16 * 256);
      sheet.setColumnWidth(4, 8 * 256);
      sheet.setColumnWidth(5, 8 * 256);
      sheet.setColumnWidth(6, 8 * 256);
      sheet.setColumnWidth(7, 30 * 256);
      sheet.setColumnWidth(8, 30 * 256);
      sheet.setColumnWidth(9, 30 * 256);
      //列宽自适应
      SXSSFWorkbook workbook = (SXSSFWorkbook) writer.getWorkbook();
      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setWrapText(true);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      for (int i = 0; i < cellList.size(); i++) {
        SXSSFCell cell1 = (SXSSFCell) writer.getCell(9, cellList.get(i));
        SXSSFCell cell2 = (SXSSFCell) writer.getCell(0, cellList.get(i));
        cell1.setCellStyle(cellStyle);
        cell2.setCellStyle(cellStyle);
      }
      for (int k = 0; k < list.size(); k++) {
        SXSSFCell cell2 = (SXSSFCell) writer.getCell(7, k + 1);
        SXSSFCell cell3 = (SXSSFCell) writer.getCell(8, k + 1);
        cell2.setCellStyle(cellStyle);
        cell3.setCellStyle(cellStyle);
      }
    }
    //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
    sheet.trackAllColumnsForAutoSizing();

    //writer.autoSizeColumnAll();
    //response为HttpServletResponse对象
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
    //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
    response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
    ServletOutputStream out = response.getOutputStream();
    // 终止后删除临时文件
    file.deleteOnExit();
    writer.flush(out, true);
    //此处记得关闭输出Servlet流
    IoUtil.close(out);
  }

  public static void downloadExcelByNum(List<Map<String, Object>> list, List<Map<String, Object>> layoutList, List<Integer> cellList, HttpServletResponse response) throws IOException {
    String tempPath = SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
    File file = new File(tempPath);
    BigExcelWriter writer = ExcelUtil.getBigWriter(file);
    layout(writer, layoutList);
    // 一次性写出内容，使用默认样式，强制输出标题
    writer.write(list, true);
    SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
    //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
    sheet.trackAllColumnsForAutoSizing();

    //writer.autoSizeColumnAll();
    //response为HttpServletResponse对象
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
    //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
    response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
    ServletOutputStream out = response.getOutputStream();
    // 终止后删除临时文件
    file.deleteOnExit();
    writer.flush(out, true);
    //此处记得关闭输出Servlet流
    IoUtil.close(out);
  }

  public static void layout(ExcelWriter writer, List<Map<String, Object>> layoutList) {
    for (int i = 0; i < layoutList.size(); i++) {
      String firstRow = layoutList.get(i).get("firstRow") + "";
      String lastRow = layoutList.get(i).get("lastRow") + "";
      String firstColumn = layoutList.get(i).get("firstColumn") + "";
      String lastColumn = layoutList.get(i).get("lastColumn") + "";
      //数字从0开始算       前面两个数字是第几行到第几行合并    后面两个数字是第几列到第几列合并
      writer.merge(Integer.valueOf(firstRow), Integer.valueOf(lastRow), Integer.valueOf(firstColumn), Integer.valueOf(lastColumn), null, true);
    }

  }
}
