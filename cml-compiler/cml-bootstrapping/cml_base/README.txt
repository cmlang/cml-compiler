This directory is here just to ensure it is not possible to replace the cml_base module installed with the CML compiler.

If the compilation of the cml_language module fails because of the cml_base module,
it is probably because it is mistakenly trying to pick it up from this location,
as opposed to finding it in CML_MODULES_PATH.