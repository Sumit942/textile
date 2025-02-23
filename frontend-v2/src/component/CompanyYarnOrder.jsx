import React, { useEffect, useState } from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { Button, TextField, IconButton, Autocomplete } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';
import { getYarnByType } from '../service/yarn';

const OrderItem = ({ control, register, index, remove }) => {
    const [itemOptions, setItemOptions] = useState([]);
    const [inputValue, setInputValue] = useState('');

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

    return (
        <div className="mb-2 border p-4 rounded-md grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2">
            <input type="hidden" {...register(`yarnOrderItems.${index}.id`)} />
            <input type="hidden" {...register(`yarnOrderItems.${index}.yarn.id`)} />
            <Autocomplete
                freeSolo
                options={itemOptions}
                getOptionLabel={(option) => option.type}
                onInputChange={(event, newInputValue) => {
                    setInputValue(newInputValue);
                }}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        {...register(`yarnOrderItems.${index}.yarn.type`, { required: "Item Name is required" })}
                        label="Item Name"
                        variant="outlined"
                        error={!!control._formState.errors?.yarnOrderItems?.[index]?.item_name}
                        helperText={control._formState.errors?.yarnOrderItems?.[index]?.item_name?.message}
                    />
                )}
            />
            <TextField
                {...register(`yarnOrderItems.${index}.quantity`, { required: "Quantity is required", min: { value: 1, message: "Quantity should be greater than 0" } })}
                label="Quantity"
                variant="outlined"
                error={!!control._formState.errors?.yarnOrderItems?.[index]?.quantity}
                helperText={control._formState.errors?.yarnOrderItems?.[index]?.quantity?.message}
            />
            <TextField
                {...register(`yarnOrderItems.${index}.boxes`, { required: "Boxes are required", min: { value: 1, message: "Boxes should be greater than 0" } })}
                label="Boxes"
                variant="outlined"
                error={!!control._formState.errors?.yarnOrderItems?.[index]?.boxes}
                helperText={control._formState.errors?.yarnOrderItems?.[index]?.boxes?.message}
            />
            <TextField {...register(`yarnOrderItems.${index}.rate`)} label="Rate" variant="outlined" />
            <TextField {...register(`yarnOrderItems.${index}.hsn`)} label="HSN Code" variant="outlined" />
            <TextField {...register(`yarnOrderItems.${index}.lot_no`)} label="Lot No" variant="outlined" />
            <TextField {...register(`yarnOrderItems.${index}.amount`)} label="Amount" variant="outlined" />
            <IconButton onClick={() => remove(index)}><Remove /></IconButton>
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

    const onSubmit = (data) => {
        console.log(data);
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
                <input type="hidden" {...register('order.id')} />
                <TextField {...register('orderDt')} label="Order Date" variant="outlined" type='date' 
                    slotProps={{ 
                        inputLabel: { 
                            shrink: true ,
                        } 
                    }}
                />
                <TextField {...register('yarnInvoiceNo')} label="Order No" variant="outlined" />
                <TextField 
                    {...register('totalQuantity', { required: "Please enter quantity" , min: { value: 1, message: "Quantity should be greater than 0" } })} 
                    label="Total Quantity" 
                    variant="outlined" 
                    error={!!methods.formState.errors.total_quantity}
                    helperText={methods.formState.errors.total_quantity?.message}
                />
                <TextField {...register('totalAmount')} label="Total Amount" variant="outlined" />
                <TextField {...register('cGst')} label="C GST" variant="outlined" />
                <TextField {...register('sGst')} label="S GST" variant="outlined" />
                <TextField {...register('iGst')} label="I GST" variant="outlined" />
                <TextField {...register('discPerc')} label="Discount" variant="outlined" />

                <div className="sm:col-span-2 grid grid-cols-1 space-y-4">
                    {fields.length > 0 && <h3 className="text-lg font-semibold">Order Items</h3>}
                    {fields.map((item, index) => (
                        <OrderItem key={item.id} control={control} register={register} index={index} remove={remove} />
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