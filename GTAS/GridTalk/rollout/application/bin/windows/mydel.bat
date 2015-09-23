rem Purpose: remove everything inside folder including subfolder but leave the root folder.
if exist %1 (
	rmdir %1 /s /q
	mkdir %1
) else (
	@echo %1 doesn't exist
)