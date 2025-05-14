import React, { useState } from 'react';
import { fetchYarnFabricDesignByYarnsAndDesigns, findYarnFabricDesignById } from '../service/yarnFabricDesign';
import { fetchProductIdAndMachineByYarnFabricDesignId } from '../service/companyYarnOrderProduct';
import { Controller, useFieldArray, useForm, useWatch } from 'react-hook-form';
import { Autocomplete, Button, TextField } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';
import { fetchOrderNoListByOrderNo } from '../service/orderApi';

const OrderProductMapping = () => {
    const methods = useForm();
    const { control, handleSubmit } = methods;
    const [yarnFabricDesignOptions, setYarnFabricDesignOptions] = useState([]);
    const [machineOptions, setMachineOptions] = useState([]);
    const [orderOptions, setOrderOptions] = useState([]);

    const { fields, append, remove } = useFieldArray({
        control,
        name: 'orderProducts'
    });

    const orderProducts = useWatch({
        control,
        name: 'orderProducts'
    });

    const fetchYarnFabricDesignsByYarnAndDesignName = (yarnAndDesignName) => {
        fetchYarnFabricDesignByYarnsAndDesigns(yarnAndDesignName)
            .then(response => setYarnFabricDesignOptions(response.data))
            .catch(error => console.log('Error fetching yarns', error));
    }

    const fetchCompanyOrderProduct = (fabricDesign) => {
        if (fabricDesign?.id) {
            fetchProductIdAndMachineByYarnFabricDesignId(fabricDesign.id)
                .then(response => setMachineOptions(response.data))
                .catch(error => {
                    alert('Error fetching machines selected fabric design');
                    console.log('Error fetching machines selected fabric design', error);
                });
        }
    }

    const fetchOrderOptions = async (orderNo) => {
        const orderNoList = await fetchOrderNoListByOrderNo(orderNo);
        setOrderOptions(orderNoList.data);
    }

    const fetchYarnFabricDesignMappingById = (index, id) => {
        findYarnFabricDesignById(id)
            .then(response => {
                const rawMaterials = response.data.map(({ yarn, percentage }) => ({ yarn, percentage }));
                methods.setValue(`orderProducts.${index}.rawMaterials`, rawMaterials);
            })
            .catch(error => console.error('Error fetching yarns', error));
    }

    const onSubmit = (data) => {
        console.log('Form data: ', data);
    }

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)} className="mx-auto mt-16 max-w-xl sm:mt-20 space-y-4">
                <div className='col-span-2 sm:col-span-4 mb-2 p-4 rounded-md grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4'>
                    <Controller
                        control={control}
                        name="orders"
                        rules={{ required: "Order No is required" }}
                        defaultValue={null}
                        render={({ field }) => (
                            <Autocomplete
                                className='col-span-2 sm:col-span-4'
                                size='small'
                                freeSolo
                                options={orderOptions}
                                getOptionLabel={(option) => option.orderNo || ''}
                                onInputChange={(_, newInputValue) => {
                                    if (newInputValue.trim() !== '') fetchOrderOptions(newInputValue);
                                }}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        label="Order No"
                                        variant="outlined"
                                        error={!!methods.formState.errors.orders}
                                        helperText={methods.formState.errors.orders?.message || ''}
                                    />
                                )}
                                onChange={(_, newValue) => field.onChange(newValue)}
                            />
                        )}
                    />
                </div>

                <div className="sm:col-span-2 grid grid-cols-1 space-y-4">
                    {fields.length > 0 && <h3 className="text-lg font-semibold">Order Products</h3>}
                    {fields.map((item, index) => {
                        
                        return (
                            <div key={item.id} className='col-span-2 sm:col-span-4 mb-2 border p-4 rounded-md grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4'>
                                <Controller
                                    control={control}
                                    name={`orderProducts.${index}.companyYarnOrderProduct.yarnFabricDesign`}
                                    rules={{ required: "Yarn Fabric Design is required" }}
                                    defaultValue={null}
                                    render={({ field }) => (
                                        <Autocomplete
                                            className='col-span-2 sm:col-span-4'
                                            size='small'
                                            freeSolo
                                            options={yarnFabricDesignOptions}
                                            getOptionLabel={(option) => option.qualityName || ''}
                                            onChange={(_, newValue) => {
                                                field.onChange(newValue);
                                                fetchCompanyOrderProduct(newValue);
                                                if (newValue?.id) {
                                                    fetchYarnFabricDesignMappingById(index, newValue.id);
                                                }
                                            }}
                                            onInputChange={(_, newInputValue) => {
                                                if (newInputValue.trim() !== '') fetchYarnFabricDesignsByYarnAndDesignName(newInputValue);
                                            }}
                                            renderInput={(params) => (
                                                <TextField
                                                    {...params}
                                                    label="Fabric Design"
                                                    variant="outlined"
                                                    error={!!methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.yarnFabricDesign}
                                                    helperText={methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.yarnFabricDesign?.message || ''}
                                                />
                                            )}
                                        />
                                    )}
                                />

                                <Controller
                                    name={`orderProducts.${index}.companyYarnOrderProduct.machine`}
                                    control={control}
                                    defaultValue={null}
                                    rules={{ required: "Machine no is required" }}
                                    render={({ field }) => (
                                        <Autocomplete
                                            {...field}
                                            className='col-span-1 sm:col-span-2'
                                            size='small'
                                            freeSolo
                                            options={machineOptions}
                                            getOptionLabel={(option) => option.machine?.machineNo ? `${option.machine.machineNo} -${option.machine.dia}/${option.machine.guage}` : ''}
                                            value={field.value?.machine || null}
                                            onChange={(_, newValue) => {
                                                if (newValue?.id) methods.setValue(`orderProducts.${index}.companyYarnOrderProduct.id`, newValue.id);
                                                if (newValue?.machine) field.onChange(newValue.machine || null);
                                            }}
                                            onInputChange={(_, newInputValue) => methods.setValue(`orderProducts.${index}.companyYarnOrderProduct.machine`, null)}
                                            renderInput={(params) => (
                                                <TextField
                                                    {...params}
                                                    label="Machine"
                                                    variant="outlined"
                                                    error={!!methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.machine}
                                                    helperText={methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.machine?.message}
                                                />
                                            )}
                                        />
                                    )}
                                />

                                <Controller
                                    name={`orderProducts.${index}.quantity`}
                                    control={control}
                                    defaultValue=''
                                    rules={{ required: 'Quantity is required', pattern: { value: /^[0-9]+$/, message: "Quantity must be a number" } }}
                                    render={({ field }) => (
                                        <TextField
                                            {...field}
                                            label="Quantity"
                                            variant="outlined"
                                            size="small"
                                            className="col-span-1 sm:col-span-2"
                                            error={!!methods.formState.errors.orderProducts?.[index]?.quantity}
                                            helperText={methods.formState.errors.orderProducts?.[index]?.quantity?.message || ''}
                                        />
                                    )}
                                />

                                <div className='col-span-2 sm:col-span-4 grid grid-cols-4 gap-x-6 gap-y-4'>    
                                {orderProducts?.[index]?.rawMaterials && orderProducts?.[index]?.rawMaterials.length > 0 && (
                                    <span className='col-span-2 sm:col-span-4'>Raw Materials</span>
                                )}
                                {orderProducts?.[index]?.rawMaterials && orderProducts?.[index]?.rawMaterials.length && 
                                    orderProducts?.[index]?.rawMaterials.map((rawMaterial, rawIndex) => (
                                        <div key={rawIndex} className='col-span-2 sm:col-span-4 space-x-2'>
                                            <TextField
                                                label="Yarn"
                                                variant="outlined"
                                                size="small"
                                                className='col-span-1 sm:col-span-2'
                                                disabled
                                                value={rawMaterial.yarn?.type || ''}
                                            />
                                            <TextField
                                                label="Percentage"
                                                variant="outlined"
                                                size="small"
                                                className='col-span-1 sm:col-span-2'
                                                disabled
                                                value={rawMaterial.percentage || ''}
                                            />
                                            //TODO: configure below field to be yarnOrderItem
                                            <Controller
                                                control={control}
                                                name={`orderProducts.${index}.rawMaterials.${rawIndex}.yarnOrderItem`}
                                                rules={{ required: "Yarn Fabric Design is required" }}
                                                defaultValue={null}
                                                render={({ field }) => (
                                                    <Autocomplete
                                                        className='col-span-2 sm:col-span-4'
                                                        size='small'
                                                        freeSolo
                                                        options={yarnFabricDesignOptions}
                                                        getOptionLabel={(option) => option.qualityName || ''}
                                                        onChange={(_, newValue) => {
                                                            field.onChange(newValue);
                                                            fetchCompanyOrderProduct(newValue);
                                                            if (newValue?.id) {
                                                                fetchYarnFabricDesignMappingById(index, newValue.id);
                                                            }
                                                        }}
                                                        onInputChange={(_, newInputValue) => {
                                                            if (newInputValue.trim() !== '') fetchYarnFabricDesignsByYarnAndDesignName(newInputValue);
                                                        }}
                                                        renderInput={(params) => (
                                                            <TextField
                                                                {...params}
                                                                label="Fabric Design"
                                                                variant="outlined"
                                                                error={!!methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.yarnFabricDesign}
                                                                helperText={methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.yarnFabricDesign?.message || ''}
                                                            />
                                                        )}
                                                    />
                                                )}
                                            />
                                        </div>
                                    ))
                                }
                                </div>
                                <Button size='small' className='col-span-2' variant="contained" color="error" onClick={() => remove(index)} startIcon={<Remove />}>
                                    Remove product
                                </Button>
                            </div>
                        );
                    })}
                    <Button size='small' className='col-span-2' variant="contained" color="primary" onClick={() => append()} startIcon={<Add />}>
                        Add product
                    </Button>
                </div>
                <Button fullWidth type="submit" variant="contained" color="secondary">
                    Save Order
                </Button>
            </form>
        </div>
    );
};

export default OrderProductMapping;