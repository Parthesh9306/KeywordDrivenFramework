'---------------------------------------------------------------------------------------------------
'# Script Name			: FT_QC_ANZ_152D2_Automation_DateStub.vbs
'# Script Description 	: This performs Date Sync in Application. 
'# Version Information	: ANZ Selenium Phase
'# Client Name			: ANZ - 4.1.000
'# Date Created  		: 10/01/2013
'# Author				: Santhosh Kumar Gande
'---------------------------------------------------------------------------------------------------
Dim objShell
Set objShell = CreateObject("wscript.shell")
'objShell.run("c:\windows\system32\cmd.exe")
'msgbox "hai we are connecting to DB and running date stub"
result = MsgBox ("You want to connect DB Or not?", vbYesNoCancel, "Yes No Example")

Select Case result
Case vbYes
    'MsgBox("You chose Yes")
	objShell.run("sqlplus /nolog @D:\Fundtech\workspace\FundTech_TAF_One\externalUtility\sysadd_sysadddays_update.stb")

Case vbNo
    'MsgBox("You chose No")

Case vbCancel
    MsgBox("Musukoooo")
End Select


'objShell.run ("connect gcptxns132/gcptxns132@anz86")
'exec date_stub;
