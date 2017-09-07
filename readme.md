# SICXE Assembler
* �䴩�G�Ҧ� SIC/XE ���O�B��Ķ�����O(START/END/BYTE/WORD/RESB/RESW/BASE)�B�򩳬۹�/PC �۹�/�X�i�w�}�B�{�����w��
* ���䴩�G��Ķ�����O(LTORG/EQU/USE/CSECT/EXTDEF/EXTREF)�B�`�ȡB�B�⦡

## �ϥΤ�k
���]sourceFilePath ���ݲ�Ķ����l�X�ɮשҦb���|�C
```
$ java SICXEASM sourceFilePath
```
## ��J�榡
* �Ҧ����e�Ҭ��j�p�g�ӷP�C
* �Ҧ� SIC/XE ���O�B��Ķ�����O �һݬ��j�g�C
* �H��.�����}�Y�� Line ���� Comment Line�C
* �C��榡��[Label]�BMnemonic�B[Operand]�B[Comment]�A�䤤�H�ťզr���j�}
* Label ���i�����A�̷ӨϥλݨD�M�w�C�ѭ^�Ʀr���u�զ��A���׳̪���6�Ӧr���A���i�P���O�W�٩μȦs���W�٬ۦP�C
* Mnemonic ���������A���D�� Line �� Comment Line�C
* Operand ���i�����A�̷ӨϥΪ����O�M�w�A���i�]�t�ťզr���A���׳̪��� 10 �Ӧr���C
* Comment ���i�����A�ݥH��.���}�Y�C 

## ��X�榡
�@��X����ɮסC
1. Assembly Listing: �R�W���uasmList.txt�v�A�s���P��l�X�ؿ��A�榡�p�ҥ� Fig2.8�C
2. Object Program: �R�W���uobjProgram.txt�v�A�s���P��l�X�ؿ��A�榡�p�ҥ� Fig2.6�C ���~��T�����q stdout ��X�C �Ҧp ![error-screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-error-screen.png)

## ���浲�G�d��
### ���ո��
���ƪ����K�A�Ȫ��I�ϡA���������usrcExample.txt�v�C
![input-screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-input-screen.png)
### ��X���G
���ƪ����K�A�Ȫ��I�ϡA���������uasmList.txt�v�B�uobjProgram.txt�v�C
![assembler-output-asmlist](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-output-asmlist-screen.png)
![assembler-output-objprogram](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/assembler-output-objprogram-screen.png)

# SICXE Linker
* �䴩�G�򥻸��J�P�s���B���w��
* ���䴩�G�ʺA�s�� 

## �ϥΤ�k
���]sourceFilePath ���ݸ��J�P�s���� ObjectProgram �ɮשҦb���|�C
```
$ java SICXELiner sourceFilePath
```
## ��J�榡
* �Ҧ����e�Ҭ��j�p�g�ӷP�C 
* �p�ҥ��ҭq H/T/D/M/R/E Record ���榡 
* �䴩�N�h�ӦP��}�� M Record �X�֦��@���C�Ҧp�N M00005706-LISTC�BM00005706+ENDC �X�֦� M00005706-LISTC +ENDC
* �u����Ū�J�@���ɮסA�Y���h�� Object Program ������X�֡C

## ��X�榡
* ���]�t�Τ��t�O�����}�� 0x4000�C
* �b�ù��W�L�X Load Map �M Memory ��T�C

## ���浲�G�d��
### ���ո��
���ƪ����K�A�Ȫ��I�ϡA���������uinputEaxmple.txt�v�C
![input screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/linker-input-screen.png)
### ��X���G
���ƪ����K�A�Ȫ��I�ϡA���������uoutputEaxmple.txt�v�C
 ![output screen](https://raw.githubusercontent.com/magiclea/SICXE/master/resources/linker-output-screen.png)

