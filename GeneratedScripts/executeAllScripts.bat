for %%G in (*.sql) do sqlcmd /S PC-SIGMA-STAG2\SQLEXPRESS /d Futura -U calystene -P calystene -i "%%G"
pause