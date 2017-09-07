# SICXE Assembler
* **支援：** 所有 SIC/XE 指令、組譯器指令(START/END/BYTE/WORD/RESB/RESW/BASE)、基底相對/PC 相對/擴展定址、程式重定位
* **不支援：** 組譯器指令(LTORG/EQU/USE/CSECT/EXTDEF/EXTREF)、常值、運算式

## 使用方法
假設sourceFilePath 為需組譯的原始碼檔案所在路徑。
```
$ java SICXEASM sourceFilePath
```
## 輸入格式
* 所有內容皆為大小寫敏感。
* 所有 SIC/XE 指令、組譯器指令 皆需為大寫。
* 以’.’為開頭的 Line 視為 Comment Line。
* 每行格式為[Label]、Mnemonic、[Operand]、[Comment]，其中以空白字元隔開
* Label 為可選欄位，依照使用需求決定。由英數字底線組成，長度最長為6個字元，不可與指令名稱或暫存器名稱相同。
* Mnemonic 為必填欄位，除非此 Line 為 Comment Line。
* Operand 為可選欄位，依照使用的指令決定，不可包含空白字元，長度最長為 10 個字元。
* Comment 為可選欄位，需以’.’開頭。 

## 輸出格式
共輸出兩個檔案。
1. Assembly Listing: 命名為「asmList.txt」，存放於同原始碼目錄，格式如課本 Fig2.8。
2. Object Program: 命名為「objProgram.txt」，存放於同原始碼目錄，格式如課本 Fig2.6。 錯誤資訊直接從 stdout 輸出。 例如：　![error-screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-error-screen.png)

## 執行結果範例
### 測試資料
為排版之便，僅附截圖，全文放於附件「srcExample.txt」。

![input-screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-input-screen.png)
### 輸出結果
為排版之便，僅附截圖，全文放於附件「asmList.txt」、「objProgram.txt」。

![assembler-output-asmlist](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-output-asmlist-screen.png)
![assembler-output-objprogram](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-output-objprogram-screen.png)

# SICXE Linker
* **支援：** 基本載入與連結、重定位
* **不支援：** 動態連結 

## 使用方法
假設sourceFilePath 為需載入與連結的 ObjectProgram 檔案所在路徑。
```
$ java SICXELiner sourceFilePath
```
## 輸入格式
* 所有內容皆為大小寫敏感。 
* 如課本所訂 H/T/D/M/R/E Record 的格式 
* 支援將多個同位址的 M Record 合併成一項。例如將 M00005706-LISTC、M00005706+ENDC 合併成 M00005706-LISTC +ENDC
* 只接受讀入一個檔案，若有多個 Object Program 須先行合併。

## 輸出格式
* 假設系統分配記憶體位址為 0x4000。
* 在螢幕上印出 Load Map 和 Memory 資訊。

## 執行結果範例
### 測試資料
為排版之便，僅附截圖，全文放於附件「inputEaxmple.txt」。

![input screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/linker-input-screen.png)
### 輸出結果
為排版之便，僅附截圖，全文放於附件「outputEaxmple.txt」。

 ![output screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/linker-output-screen.png)

