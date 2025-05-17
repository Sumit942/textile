import React, { useState } from 'react';
import { Controller, useFieldArray, useForm, useWatch } from 'react-hook-form';
import { Autocomplete, Button, TextField, Dialog, DialogTitle, DialogContent, TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, Checkbox } from '@mui/material';
import { Add, Remove } from '@mui/icons-material';
import { fetchOrderNoListByOrderNo } from '../service/orderApi';
import { fetchYarnFabricDesignByYarnsAndDesigns, findYarnFabricDesignById } from '../service/yarnFabricDesign';
import { fetchProductIdAndMachineByYarnFabricDesignId } from '../service/companyYarnOrderProduct';

const OrderProductMapping = () => {
    const methods = useForm();
    const { control, handleSubmit } = methods;
    const [orderOptions, setOrderOptions] = useState([]);

    const { fields, append, remove } = useFieldArray({
        control,
        name: 'orderProducts'
    });

    const orderProducts = useWatch({
        control,
        name: 'orderProducts'
    });
    const selectedOrder = useWatch({ control, name: "orders" });

    const fetchOrderOptions = async (orderNo) => {
        const orderNoList = await fetchOrderNoListByOrderNo(orderNo);
        setOrderOptions(orderNoList.data);
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
                    {fields.map((item, index) => (
                        <OrderProductMappingItem
                            key={item.id}
                            index={index}
                            control={control}
                            methods={methods}
                            selectedOrder={selectedOrder}
                            orderProducts={orderProducts}
                            remove={remove}
                        />
                    ))}
                    <Button
                        size='small'
                        className='col-span-2'
                        variant="contained"
                        color="primary"
                        onClick={() => append()}
                        startIcon={<Add />}
                        disabled={!selectedOrder}
                    >
                        Add product
                    </Button>
                </div>
                <Button
                    fullWidth
                    type="submit"
                    variant="contained"
                    color="secondary"
                    disabled={!selectedOrder}
                >
                    Save Order
                </Button>
            </form>
        </div>
    );
};

export default OrderProductMapping;

const OrderProductMappingItem = ({ 
    index, 
    control, 
    methods,
    selectedOrder,
    orderProducts,
    remove 
}) => {
    const [yarnFabricDesignOptions, setYarnFabricDesignOptions] = useState([]);
    const [machineOptions, setMachineOptions] = useState([]);

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

    const fetchYarnFabricDesignMappingById = (id) => {
        findYarnFabricDesignById(id)
            .then(response => {
                const rawMaterials = response.data.map(({ yarn, percentage }) => ({ yarn, percentage }));
                methods.setValue(`orderProducts.${index}.rawMaterials`, rawMaterials);
            })
            .catch(error => console.error('Error fetching yarns', error));
    }

    return (
        <div className='col-span-2 sm:col-span-4 mb-2 border p-4 rounded-md grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4'>
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
                                fetchYarnFabricDesignMappingById(newValue.id);
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
                                disabled={!selectedOrder}
                            />
                        )}
                        disabled={!selectedOrder}
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
                                disabled={!selectedOrder}
                            />
                        )}
                        disabled={!selectedOrder}
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
                        disabled={!selectedOrder}
                    />
                )}
            />
            
            <div className='col-span-2 sm:col-span-4 grid grid-cols-4 gap-x-6 gap-y-4'>    
                {orderProducts?.[index]?.rawMaterials?.length > 0 && (
                    <span className='col-span-2 sm:col-span-4'>Raw Materials</span>
                )}
                {orderProducts?.[index]?.rawMaterials?.map((rawMaterial, rawIndex) => (
                    <RawMaterial 
                        key={rawIndex} 
                        rawMaterial={rawMaterial}
                        index={index}
                        rawIndex={rawIndex}
                        control={control} 
                        methods={methods}
                    />
                ))}
            </div>
            <Button
                size='small'
                className='col-span-2'
                variant="contained"
                color="error"
                onClick={() => remove(index)}
                startIcon={<Remove />}
                disabled={!selectedOrder}
            >
                Remove product
            </Button>
        </div>
    );
};

const RawMaterial = ({ 
    rawMaterial,
    index,
    rawIndex,
    control,
    methods
}) => {
    const [open, setOpen] = useState(false);
    const [yarnOrderItems, setYarnOrderItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleSelectItem = (item) => {
        const selectedIndex = selectedItems.findIndex(i => i.id === item.id);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = [...selectedItems, item];
        } else {
            newSelected = selectedItems.filter(i => i.id !== item.id);
        }

        setSelectedItems(newSelected);
    };

    const handleSave = () => {
        methods.setValue(
            `orderProducts.${index}.rawMaterials.${rawIndex}.selectedOrders`, 
            selectedItems
        );
        handleClose();
    };

    return (
        <div className='col-span-2 sm:col-span-4 space-x-2'>
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
            
            <Button
                variant="outlined"
                onClick={handleOpen}
                className='col-span-2 sm:col-span-4'
            >
                Select Yarn Orders
            </Button>

            <Dialog 
                open={open}
                onClose={handleClose}
                maxWidth="md"
                fullWidth
            >
                <DialogTitle>Select Yarn Orders</DialogTitle>
                <DialogContent>
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell padding="checkbox">
                                        <Checkbox 
                                            checked={yarnOrderItems.length > 0 && 
                                                selectedItems.length === yarnOrderItems.length}
                                            onChange={(e) => {
                                                if (e.target.checked) {
                                                    setSelectedItems(yarnOrderItems);
                                                } else {
                                                    setSelectedItems([]);
                                                }
                                            }}
                                        />
                                    </TableCell>
                                    <TableCell>Order No</TableCell>
                                    <TableCell>Quantity</TableCell>
                                    <TableCell>Quantity Allocated</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {yarnOrderItems.map((item) => (
                                    <TableRow 
                                        key={item.id}
                                        hover
                                        onClick={() => handleSelectItem(item)}
                                    >
                                        <TableCell padding="checkbox">
                                            <Checkbox 
                                                checked={selectedItems.some(i => i.id === item.id)}
                                            />
                                        </TableCell>
                                        <TableCell>{item.orderNo}</TableCell>
                                        <TableCell>{item.quantity}</TableCell>
                                        <TableCell>{item.quantityAllocated}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>

                    <div className="flex justify-end gap-2 mt-4">
                        <Button onClick={handleClose}>Cancel</Button>
                        <Button 
                            variant="contained" 
                            color="primary"
                            onClick={handleSave}
                        >
                            Save
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>

            {/* Display selected items summary */}
            {selectedItems.length > 0 && (
                <div className="col-span-2 sm:col-span-4 mt-2">
                    <Typography variant="subtitle2">
                        Selected Orders: {selectedItems.length}
                    </Typography>
                </div>
            )}
        </div>
    );
};