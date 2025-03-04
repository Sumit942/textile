import React, { useEffect, useState } from 'react';
import { useForm, useFieldArray, Controller } from 'react-hook-form';
import { Button, TextField, Autocomplete } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';
import { getYarnByType } from '../service/yarn';
import { saveCompanyYarnOrder } from '../service/companyYarnOrder';
import { fetchOrderNoListByOrderNo } from '../service/orderApi';
import { fetchYarnFabricDesignByYarnsAndDesigns } from '../service/yarnFabricDesign';

const OrderItem = ({ control, methods, register, index, remove }) => {
    const [itemOptions, setItemOptions] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [yarnFabricDesignOptions, setYarnFabricDesignOptions] = useState([]);
    const { 
        fields: yarnOrderItemProducts,
        append: appendYarnOrderItemProducts,
        remove: removeYarnOrderItemProducts 
    } = useFieldArray({
        control,
        name: `yarnOrderItems.${index}.yarnOrderItemProducts`
    });

    useEffect(() => {
        if (inputValue) {
            fetchYarnByName(inputValue)
        }
    }, [inputValue]);

    const fetchYarnByName = async (name) => {
        try {
            const response = await getYarnByType(name);
            setItemOptions(response.data);
        } catch (error) {
            console.error('Error fetching yarn:', error);
        }
    }

    const fetchYarnFabricDesignsByYarnAndDesignName = (yarnAndDesignName) => {
        fetchYarnFabricDesignByYarnsAndDesigns(yarnAndDesignName)
            .then(response => setYarnFabricDesignOptions(response.data))
            .catch(error => {
                console.log('Error fetching yarns', error);
            })
    }

    return (
        <div className="mb-2 border p-4 rounded-md grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4">
            <input type="hidden" {...register(`yarnOrderItems.${index}.id`)} />
            <input type="hidden" {...register(`yarnOrderItems.${index}.yarn.id`, {required : 'Please search and select yarn'})} />
            <Autocomplete
                className='col-span-2'
                size='small'
                freeSolo
                options={itemOptions}
                getOptionLabel={(option) => option?.type || ''}
                onChange={(event, newValue) => {
                    if (newValue) {
                        methods.setValue(`yarnOrderItems.${index}.yarn.id`, newValue.id);
                        methods.setValue(`yarnOrderItems.${index}.yarn.type`, newValue.type);
                        methods.clearErrors(`yarnOrderItems.${index}.yarn.id`);
                    }
                }}
                onInputChange={(event, newInputValue) => {
                    setInputValue(newInputValue.trim());
                    methods.setValue(`yarnOrderItems.${index}.yarn.id`, '');
                }}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        {...register(`yarnOrderItems.${index}.yarn.type`, { required: "Item Name is required" })}
                        label="Item Name"
                        variant="outlined"
                        error={!!control._formState.errors?.yarnOrderItems?.[index]?.yarn?.type || !!control._formState.errors?.yarnOrderItems?.[index]?.yarn?.id}
                        helperText={control._formState.errors?.yarnOrderItems?.[index]?.yarn?.type?.message || control._formState.errors?.yarnOrderItems?.[index]?.yarn?.id?.message}
                    />
                )}
            />
            <TextField size='small' {...register(`yarnOrderItems.${index}.hsn`)} label="HSN Code" variant="outlined" />
            <TextField size='small' {...register(`yarnOrderItems.${index}.lot_no`)} label="Lot No" variant="outlined" />
            <TextField
                size='small'
                {...register(`yarnOrderItems.${index}.quantity`, { required: "Quantity is required", min: { value: 1, message: "Quantity should be greater than 0" } })}
                label="Quantity"
                variant="outlined"
                error={!!control._formState.errors?.yarnOrderItems?.[index]?.quantity}
                helperText={control._formState.errors?.yarnOrderItems?.[index]?.quantity?.message}
            />
            <TextField
                size='small'
                {...register(`yarnOrderItems.${index}.boxes`, { required: "Boxes are required", min: { value: 1, message: "Boxes should be greater than 0" } })}
                label="Boxes"
                variant="outlined"
                error={!!control._formState.errors?.yarnOrderItems?.[index]?.boxes}
                helperText={control._formState.errors?.yarnOrderItems?.[index]?.boxes?.message}
            />
            <TextField size='small' {...register(`yarnOrderItems.${index}.rate`)} label="Rate" variant="outlined" />
            <TextField size='small' {...register(`yarnOrderItems.${index}.amount`)} label="Amount" variant="outlined" />

            
            {yarnOrderItemProducts?.length > 0 && <h3 className="font-semibold col-span-2 sm:col-span-4">Order Item Products</h3>}
            {yarnOrderItemProducts.map((item, itemIndex) => (
                <div key={`${index}-${item.id}`} className='col-span-2 sm:col-span-4 grid grid-cols-4 gap-x-6 gap-y-4'>
                    <Controller
                        control={control}
                        name={`yarnOrderItems.${index}.yarnOrderItemProducts.${itemIndex}`}
                        defaultValue={null}
                        render={({ field }) => (
                            <Autocomplete
                                className='col-span-3'
                                size='small'
                                freeSolo
                                options={yarnFabricDesignOptions}
                                getOptionLabel={(option) => option.qualityName || ''}
                                onChange={(_, newValue) => {
                                    field.onChange(newValue)
                                }}
                                onInputChange={(event, newInputValue) => {
                                    if (newInputValue.trim() !== '') {
                                        fetchYarnFabricDesignsByYarnAndDesignName(newInputValue);
                                    }
                                }}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        label="Fabric Design"
                                        variant="outlined"
                                        error={!!methods.formState.errors.yarnOrderItems?.[index]?.yarnOrderItemProducts?.[itemIndex]}
                                        helperText={methods.formState.errors.yarnOrderItems?.[index]?.yarnOrderItemProducts?.[itemIndex]?.message || ''}
                                    />
                                )}
                            />)}
                    />
                    <Button size='small' className='col-span-1' variant="contained" color="error" onClick={() => removeYarnOrderItemProducts(index)}
                        startIcon={<Remove />}
                    ></Button>
                </div>
            ))}
            <Button size='small' className='col-span-2' 
                variant="contained" color="primary" onClick={() => appendYarnOrderItemProducts(index)}
                startIcon={<Add />}
            >
                fabric design quality
            </Button>
            <Button className='col-span-2' variant="contained" color="error" onClick={() => remove(index)} startIcon={<Remove />}>
                Remove Item
            </Button>
        </div>
    );
};

const CompanyYarnOrder = () => {
    const methods = useForm();
    const { control, register, handleSubmit } = methods;
    const { fields, append, remove } = useFieldArray({
        control,
        name: 'yarnOrderItems'
    });
    const [orderOptions, setOrderOptions] = useState([]);

    const fetchOrderOptions = async (orderNo) => {
        const orderNoList = await fetchOrderNoListByOrderNo(orderNo);
        setOrderOptions(orderNoList.data);
    }

    const onSubmit = async (data) => {

        if (data.yarnOrderItems.length === 0) {
            alert('Please add atleast one item')
            return;
        }
        console.log('Company Yarn Order Data:', data);
        return;
       const response = await saveCompanyYarnOrder(data);
       console.log('Save Company Yarn Order Response:', response);
       if (response.status === 201) {
           alert('Company Yarn Order saved successfully');
       } else {
           const errors = response.response.data.errors;
           if (errors) {
                Object.keys(errors).forEach((field) => {
                    methods.setError(field, {
                        type: "server",
                        message: errors[field].join(", "),
                    });
                });
            }
           alert('Error saving Company Yarn Order');
       }
    };

    return (
        <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
        <div className="mx-auto max-w-2xl text-center">
            <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
            Save Company Yarn Order
            </h2>
        </div> 
            <form onSubmit={handleSubmit(onSubmit)} className="mx-auto mt-16 max-w-xl sm:mt-20 space-y-4">
            <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
                <input type="hidden" {...register('id')} />
                <Controller
                    control={control}
                    name='order'
                    rules={{
                        required: 'Please select orderNo'
                    }}
                    render={( { field } ) => (
                        <Autocomplete
                            {...field}
                            value={field?.orderNo || ''}
                            freeSolo
                            options={orderOptions}
                            getOptionLabel={(option) => option.orderNo || ''}
                            onInputChange={(_, newInputValue) => {
                                if (newInputValue.trim() !== '') {
                                    fetchOrderOptions(newInputValue);
                                }
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Order No"
                                    variant="outlined"
                                    error={!!methods.formState.errors.order}
                                    helperText={methods.formState.errors.order?.message || ''}
                                />
                            )}
                            onChange={(_, newValue) => {
                                if (newValue) {
                                    field.onChange(newValue)
                                }
                            }}
                        />
                    )}
                />
                <TextField {...register('orderDt')} label="Order Date" variant="outlined" type='date' 
                    slotProps={{ 
                        inputLabel: { 
                            shrink: true ,
                        } 
                    }}
                />
                <TextField 
                    {...register('totalQuantity', { required: "Please enter quantity" , min: { value: 1, message: "Quantity should be greater than 0" } })} 
                    label="Total Quantity" 
                    variant="outlined" 
                    error={!!methods.formState.errors.totalQuantity}
                    helperText={methods.formState.errors.totalQuantity?.message}
                />
                <TextField {...register('totalAmount')} label="Total Amount" variant="outlined" />
                <TextField {...register('cGst')} label="C GST" variant="outlined" />
                <TextField {...register('sGst')} label="S GST" variant="outlined" />
                <TextField {...register('iGst')} label="I GST" variant="outlined" />
                <TextField {...register('discPerc')} label="Discount" variant="outlined" />

                <div className="sm:col-span-2 grid grid-cols-1 space-y-4">
                    {fields.length > 0 && <h3 className="text-lg font-semibold">Order Items</h3>}
                    {fields.map((item, index) => (
                        <OrderItem key={item.id} control={control} methods={methods} register={register} index={index} remove={remove} />
                    ))}
                </div>
                <TextField {...register('remark')} label="Remark" variant="outlined" multiline rows={4} />
                </div>
                <div className="mt-4 space-x-4">
                    <Button variant="contained" color="primary" onClick={() => append({})} startIcon={<Add />}>
                        Add Item
                    </Button>

                    <Button type="submit" variant="contained" color="secondary">
                        Save Order
                    </Button>
                </div>
            </form>
        </div>
    );
};

export default CompanyYarnOrder;