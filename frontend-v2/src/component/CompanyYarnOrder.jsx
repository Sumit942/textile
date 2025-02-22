import React from 'react';
import { useForm, useFieldArray } from 'react-hook-form';
import { Button, TextField, IconButton } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';

const CompanyYarnOrder = () => {
    const { register, control, handleSubmit, reset } = useForm({
        defaultValues: {
            order_date: '',
            order_no: '',
            total_quantity: '',
            total_amount: '',
            c_gst: '',
            s_gst: '',
            i_gst: '',
            order_items: [{ item_name: '', hsn_code: '', lot_no: '', quantity: '', rate: '', discount: '', boxes: '', amount: '' }]
        }
    });
    const { fields, append, remove } = useFieldArray({
        control,
        name: 'order_items'
    });

    const onSubmit = (data) => {
        console.log(data);
        reset();
    };

    return (
        <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
            <div className="mx-auto max-w-2xl text-center">
                <h2 className="text-balance text-2xl font-semibold tracking-tight text-gray-900 sm:text-3xl">
                    Save Company Yarn Order
                </h2>
            </div> 
            <form onSubmit={handleSubmit(onSubmit)} className="mx-auto mt-16 max-w-xl sm:mt-20 space-y-4 lg:mt-24">
            <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
                <TextField
                    label="Order Date"
                    type="date"
                    InputLabelProps={{ shrink: true }}
                    {...register('order_date', { required: 'Order Date is required' })}
                    className="w-full"
                />
                <TextField
                    label="Order No"
                    variant="outlined"
                    {...register('order_no')}
                    className="w-full"
                />
                <TextField
                    label="Total Quantity"
                    variant="outlined"
                    {...register('total_quantity', { required: 'Total Quantity is required' })}
                    className="w-full" 
                />
                <TextField
                    label="Total Amount"
                    variant="outlined"
                    {...register('total_amount', { required: 'Total Amount is required' })}
                    className="w-full"
                />
                <TextField
                    label="C GST"
                    variant="outlined"
                    {...register('c_gst')}
                    className="w-full"
                />
                <TextField
                    label="S GST"
                    variant="outlined"
                    {...register('s_gst')}
                    className="w-full"
                />
                <TextField
                    label="I GST"
                    variant="outlined"
                    {...register('i_gst')}
                    className="w-full"
                />

                {fields.map((item, index) => (
                    <div key={item.id} className="space-y-2">
                        <div className="flex items-center space-x-2">
                            <TextField
                                label="Item Name"
                                variant="outlined"
                                {...register(`order_items.${index}.item_name`)}
                                className="flex-1"
                            />
                            <TextField
                                label="HSN Code"
                                variant="outlined"
                                {...register(`order_items.${index}.hsn_code`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Lot No"
                                variant="outlined"
                                {...register(`order_items.${index}.lot_no`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Quantity"
                                variant="outlined"
                                {...register(`order_items.${index}.quantity`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Rate"
                                variant="outlined"
                                {...register(`order_items.${index}.rate`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Discount"
                                variant="outlined"
                                {...register(`order_items.${index}.discount`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Boxes"
                                variant="outlined"
                                {...register(`order_items.${index}.boxes`)}
                                className="flex-1"
                            />
                            <TextField
                                label="Amount"
                                variant="outlined"
                                {...register(`order_items.${index}.amount`)}
                                className="flex-1"
                            />
                            <IconButton onClick={() => remove(index)}>
                                <Remove />
                            </IconButton>
                        </div>
                    </div>
                ))}
                </div>
                <Button
                    type="button"
                    variant="contained"
                    color="primary"
                    onClick={() => append({ item_name: '', hsn_code: '', lot_no: '', quantity: '', rate: '', discount: '', boxes: '', amount: '' })}
                    startIcon={<Add />}
                >
                    Add Item
                </Button>
                <Button type="submit" variant="contained" color="secondary">
                    Save Order
                </Button>
            </form>
        </div>
    );
};

export default CompanyYarnOrder;