if exist %1 (
	rmdir %1 /s /q
) else (
	@echo %1 doesn't exist
)