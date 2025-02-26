import React, { useState, useEffect } from 'react';
import { useForm, Controller, useFieldArray } from 'react-hook-form';
import { TextField, Button, Autocomplete, Box, Typography } from '@mui/material';
import { useParams } from 'react-router-dom';
import { findYarnFabricDesignById, saveYarnFabricDesign } from '../service/yarnFabricDesign';
import { getYarnByType } from '../service/yarn';
import { getFabricDesignsByNameLike } from '../service/fabricDesign';

const YarnFabricDesign = () => {
    const { id } = useParams();
    const { control, handleSubmit, setError, setValue, formState: { errors } } = useForm();
    const { fields, append, remove } = useFieldArray({
        control,
        name: 'yarns'
    });

    const [yarnOptions, setYarnOptions] = useState([]);
    const [designOptions, setDesignOptions] = useState([]);

    useEffect(() => {
        console.log('useEffect() Entry: ', id);
        //TODO: setValue after fetching data
        if (id) {
            findYarnFabricDesignById(id)
                .then(response => {
                    const data = response.data;
                    setValue('id', data.id);
                    setValue('fabricDesign', data.fabricDesign);
                    setValue('gsm', data.gsm);
                    data.yarns.forEach((yarn, index) => {
                        append({ yarn });
                    });
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        } else {
            append({ type: '' })
        }
    }, [id]);

    const onSubmit = async (data) => {
        console.log('onSubmit() Entry: ', data);
        return;
        try {
            const response = await saveYarnFabricDesign(data);
            if (response.status === 200) {
                alert('Yarn Fabric Design saved successfully');
            } else if (response.status === 400) {
                setError('apiError', { message: 'Bad Request' });
            } else {
                setError('apiError', { message: 'An error occurred' });
            }
        } catch (error) {
            setError('apiError', { message: 'An error occurred' });
        }
    };

    const fetchYarnOptions = async (inputValue) => {
        try {
            const response = await getYarnByType(inputValue);
            setYarnOptions(response.data);
        } catch (error) {
            console.error('Error fetching yarn options:', error);
        }
    };

    const fetchDesignOptions = async (inputValue) => {
        try {
            const response = await getFabricDesignsByNameLike(inputValue);
            setDesignOptions(response.data);
        } catch (error) {
            console.error('Error fetching design options:', error);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)}
            className='isolate bg-white px-6 py-5 sm:py-10 lg:px-8'
        >
            <Typography variant="h6">Yarn Fabric Design Form</Typography>
            <Controller
                name="quality"
                control={control}
                defaultValue=""
                render={({ field }) => (
                    <TextField {...field} label="Quality" slotProps={{ input: { readOnly: true } }} />
                )}
            />
            <Controller
                name="id"
                control={control}
                render={({ field }) => (
                    <input type="hidden" {...field} />
                )}
            />
            {fields.map((item, index) => (
                <Box key={item.id} >
                    <Controller
                        name={`yarns[${index}].id`}
                        control={control}
                        render={({ field }) => (
                            <input type="hidden" {...field} />
                        )}
                    />
                    <Controller
                        name={`yarns[${index}].type`}
                        control={control}
                        defaultValue="" // Ensuring controlled component
                        render={({ field: { onChange, value } }) => (
                            <Autocomplete
                                options={yarnOptions}
                                getOptionLabel={(option) => option?.type || ''}
                                value={yarnOptions.find(y => y.type === value) || null} // Ensuring value consistency
                                onChange={(_, selectedOption) => {
                                    setValue(`yarns[${index}].id`, selectedOption?.id || '');
                                    setValue(`yarns[${index}].type`, selectedOption?.type || '');
                                    onChange(selectedOption?.type || ''); // Ensuring React Hook Form state updates correctly
                                }}
                                renderInput={(params) => (
                                    <TextField 
                                        {...params} 
                                        label="Product (Yarn)" 
                                        onChange={(e) => fetchYarnOptions(e.target.value)} 
                                    />
                                )}
                            />
                        )}
                    />


                    <Button onClick={() => remove(index)} >Remove</Button>
                </Box>
            ))}
            <Button onClick={() => append({ id: '', type: '' })}>Add Yarn</Button>
            <Controller
                name="fabricDesign"
                control={control}
                defaultValue={null} // Ensuring controlled behavior
                render={({ field: { onChange, value } }) => (
                    <Autocomplete
                        options={designOptions}
                        getOptionLabel={(option) => option?.name || ''}
                        value={designOptions.find(option => option.name === value) || null} // Ensure consistency
                        onChange={(_, selectedOption) => {
                            onChange(selectedOption?.name || ''); // Sync with React Hook Form
                        }}
                        renderInput={(params) => (
                            <TextField 
                                {...params} 
                                label="fabricDesign" 
                                onChange={(e) => fetchDesignOptions(e.target.value)} 
                            />
                        )}
                    />
                )}
            />

            <Controller
                name="gsm"
                defaultValue=''
                control={control}
                render={({ field }) => (
                    <TextField {...field} label="GSM" error={!!errors.gsm} helperText={errors.gsm ? errors.gsm.message : ''} />
                )}
            />
            {errors.apiError && <Typography color="error">{errors.apiError.message}</Typography>}
            <Button type="submit" variant="contained" sx={{ mt: 2 }}>{id ? 'Update' : 'Save'}</Button>
        </Box>
    );
};

export default YarnFabricDesign;