import { useState } from 'react';
import { useForm, Controller, useFieldArray } from 'react-hook-form';
import { TextField, Autocomplete, Snackbar, Alert, Typography, Button, Box, Grid2 } from '@mui/material';
import { getYarnByType } from '../service/yarn'
import { getFabricDesignsByNameLike } from '../service/fabricDesign'
import { saveYarnFabricDesign } from '../service/yarnFabricDesign';
import { Add, Remove } from '@mui/icons-material';

const YarnFabricDesign = () => {
    const { handleSubmit, control, watch, reset, setError, formState: { errors, isSubmitting } } = useForm({
        defaultValues: {
            qualityName: '',
            fabricDesignYarnMappings: [
                {
                    yarn: null,
                    percentage: ''
                }
            ],
            fabricDesign: null,
            gsm: '',
        }
    });

    const { fields: fabricDesignYarnMappingsField,
        append,
        remove
    } = useFieldArray({
        control,
        name: "fabricDesignYarnMappings"
    })

    const [yarnOptions, setYarnOptions] = useState([]);
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [designOptions, setDesignOptions] = useState([]);
    const [openRecordExistsSnackbar, setOpenRecordExistsSnackbar] = useState(false)

    const fetchYarnOptions = async (inputValue) => {
        try {
            const response = await getYarnByType(inputValue);
            setYarnOptions(response.data);
        } catch (error) {
            console.error('Error fetching yarn options:', error);
            setYarnOptions([])
        }
    };

    const fetchDesignOptions = async (inputValue) => {
        try {
            const response = await getFabricDesignsByNameLike(inputValue);
            console.log('fetchDesignOptions response: ', response.data)
            setDesignOptions(response.data);
        } catch (error) {
            console.error('Error fetching design options:', error);
        }
    };

    const selectedYarns = watch('fabricDesignYarnMappings')
    const selectedDesign = watch('fabricDesign')
    const enteredGsm = watch('gsm')

    const getFullQualityName = () => {
        const selectedYarnStr = selectedYarns ? selectedYarns.map((mapping) => mapping.yarn?.type || '').join(" x ") : '';
        const selectedDesignStr = selectedDesign ? selectedDesign.name : '';
        return selectedYarnStr + ' --' + selectedDesignStr + " (Gsm:"+ enteredGsm + ")";
    }

    const onSubmit = async (data) => {
        const totalPerc = selectedYarns.reduce((sum, item) => {
            const percentageValue = parseFloat(item.percentage) || 0;
            return sum + percentageValue;
        }, 0)
        console.log('totalPerc: ', totalPerc)
        if (totalPerc !== 100.00) {
            setError(`fabricDesignYarnMappings.[0].percentage`, { type: 'manual', message: 'Sum all percentage should be 100'})
        }
        console.log('onSumbit: ', data);
        // saveYarnFabricDesign(data)
        //     .then(response => {
        //         console.log("sucess response: ", response)
        //         alert("Yarn Fabric Design saved sucessfully!!")
        //     })
        //     .catch(error => {
        //         console.log('Error saving yarnFabricDesign: ', error)
        //         if (error.status == 400) {
        //             const isRecordExist = error.response?.data?.errorMessages?.yarnFabricDesign ? true : false;
        //             setOpenRecordExistsSnackbar(true)
        //         } else {
        //             alert('Error Saving')
        //         }
        //     })

    };

    const isDuplicateYarn = (yarnobj) => {
        console.log('isDuplicateYarn: ', yarnobj)
        const val = selectedYarns.filter((selectedYarn) => selectedYarn.yarn?.id === yarnobj?.id )
        return val.length > 0 && val[0]?.yarn?.id ? true: false;
    }

    console.log('erros; :: ', errors)

    return (
        <form 
            className='mx-auto mt-16 max-w-xl sm:mt-20'
            onSubmit={handleSubmit(onSubmit)}
        >
            <div className='px-3.5 py-2'>
            <Controller
                name="qualityName"
                control={control}
                render={({ field }) => (
                <TextField
                    {...field}
                    label="Quality"
                    variant="filled"
                    disabled
                    value={getFullQualityName()}
                    className='w-full'
                />
                )}
            />
            </div>
            <div className='px-3.5 py-2'>
            <Typography component={'h2'} marginTop={3} textAlign={'center'} >Yarns</Typography>
            {fabricDesignYarnMappingsField.map((yarnMapping, index)  => {
                return (
                <Grid2 key={index} container spacing={2} marginTop={2}>
                    <Grid2 size={6}>
                    <Controller
                        name={`fabricDesignYarnMappings.${index}.yarn`}
                        control={control}
                        rules={{ required: 'Yarns are required' }}
                        defaultValue={yarnMapping.yarn}
                        render={({ field }) => (
                            <Autocomplete
                                {...field}
                                value={field.value || ''}
                                options={yarnOptions}
                                getOptionLabel={(option) => option.type || ''}
                                size='small'
                                onInputChange={(_, value) => {
                                    if (value.trim()) {
                                        fetchYarnOptions(value)
                                    }
                                }}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        label="Yarns"
                                        placeholder='Select Yarns'
                                        error={!!errors.fabricDesignYarnMappings?.[index]?.yarn}
                                        helperText={errors.fabricDesignYarnMappings?.[index]?.yarn?.message || ''}
                                    />
                                )}
                                onChange={(_, value) => {
                                    if (isDuplicateYarn(value)) {
                                        document.activeElement.blur();
                                        setOpenSnackbar(true)
                                        field.onChange(field.value)
                                    } else {
                                        field.onChange(value)
                                    }
                                }}
                            />
                        )}
                    />
                    </Grid2>
                    <Grid2 size={4} alignContent={'center'}>
                    <Controller
                        name={`fabricDesignYarnMappings.${index}.percentage`}
                        control={control}
                        rules={{
                            required: 'Please enter percentage',
                            min: { value: 0.01, message: 'Value should be at least 0.01' },
                            max: { value: 100.00, message: 'Value should not greater than 100' }
                        }}
                        defaultValue={yarnMapping.percentage}
                        render={({ field }) => (
                            <TextField
                                size='small'
                                {...field}
                                type="number"
                                label="Percentage"
                                error={!!errors.fabricDesignYarnMappings?.[index]?.percentage?.message}
                                helperText={errors.fabricDesignYarnMappings?.[index]?.percentage?.message || ''}
                            />
                        )}
                    />
                    </Grid2>
                    <Grid2 size={1}>
                    <Button
                        variant='contained'
                        color='error'
                        onClick={() => remove(index)}
                        disabled={fabricDesignYarnMappingsField.length === 1}
                        startIcon={<Remove />}
                    ></Button>
                    </Grid2>
                </Grid2>
                )
            })
            }
            </div>
            <div className='px-3.5 py-2'>
                <Button variant='contained' startIcon={<Add />}
                    onClick={() => append({ yarn: null, percentage: ''})}
                >Add Yarn</Button>
            </div>
            <Grid2 className='px-3.5 py-2' container spacing={2}>
            <Grid2 size={8}>
            <Controller
                name="fabricDesign"
                control={control}
                rules={{ required: 'Design is required' }}
                // defaultValue={null}
                render={({ field }) => (
                    <Autocomplete
                        {...field}
                        options={designOptions}
                        getOptionLabel={(option) => option?.name || ''}
                        onInputChange={(event, value) => {
                            if (value.trim() !== '') {
                                fetchDesignOptions(value)
                            }
                        }}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                label="Design"
                                error={!!errors.fabricDesign}
                                helperText={errors.fabricDesign ? errors.fabricDesign.message : ''}
                            />
                        )}
                        onChange={(event, value) => field.onChange(value)}
                    />
                )}
            />
            </Grid2>
            <Grid2 size={4}>
            <Controller
                name="gsm"
                control={control}
                // defaultValue=""
                rules={{
                    required: "GSM is required",
                }}
                render={({ field }) => (
                <TextField
                    {...field}
                    label="GSM"
                    variant="outlined"
                    error={!!errors.gsm}
                    helperText={errors.gsm?.message || ''}
                />
                )}
            />
            </Grid2>
            </Grid2>

            <div className='px-3.5 py-2 space-y-2'>
            <button 
                className="mb-3 w-full border rounded-md bg-gray-100 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
                type="button"
                onClick={() => reset()}
                >
                Reset
            </button>
            <button 
                className="block w-full rounded-md bg-indigo-600 px-3.5 py-3 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                type="submit"
                disabled={isSubmitting}
                >
                {isSubmitting ? 'Submitting...' : 'Submit'}
            </button>
            </div>
            <Snackbar
                open={openSnackbar}
                autoHideDuration={3000}
                onClose={() => setOpenSnackbar(false)}
                message="Yarn already selected!"
            />
            <Snackbar
                open={openRecordExistsSnackbar}
                autoHideDuration={3000} // Automatically hide after 3 seconds
                onClose={() => setOpenRecordExistsSnackbar(false)} // Close Snackbar when dismissed
                anchorOrigin={{ vertical: "top", horizontal: "center" }} // Position of Snackbar
            >
                <Alert severity="error" onClose={() => setOpenRecordExistsSnackbar(false)}>
                The record already exists in the system!
                </Alert>
            </Snackbar>
        </form>
    );
};

export default YarnFabricDesign;