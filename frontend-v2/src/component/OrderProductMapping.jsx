import React, { useState, useEffect } from 'react';
import { Controller, useFieldArray, useForm, useWatch } from 'react-hook-form';
import { Autocomplete, Button, TextField, Dialog, DialogTitle, DialogContent, TableContainer, Table, TableHead, TableBody, TableRow, TableCell, Paper, Checkbox, Typography, Snackbar, Alert } from '@mui/material';
import { Add, Refresh, Remove } from '@mui/icons-material';
import { fetchOrderNoListByOrderNo } from '../service/orderApi';
import { fetchYarnFabricDesignByYarnsAndDesigns, fetchFabricDesignYarnMappingById } from '../service/yarnFabricDesign';
import { fetchProductIdAndMachineByYarnFabricDesignId } from '../service/companyYarnOrderProduct';
import { fetchYarnOrderItemsByOrderAndYarnType } from '../service/yarnOrderItem';
import { saveOrderProductMapping } from '../service/orderProductMapping';

const OrderProductMapping = () => {
    const methods = useForm();
    const { control, handleSubmit } = methods;
    const [orderOptions, setOrderOptions] = useState([]);
    const [alertState, setAlertState] = useState({
        open: false,
        message: '',
        severity: 'success'
    });
    const [isSubmitting, setIsSubmitting] = useState(false);

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

    const handleAlertClose = () => {
        setAlertState({...alertState, open: false});
    };

    const onSubmit = async (data) => {
        setIsSubmitting(true);
        try {
            const response = await saveOrderProductMapping(data);
            if (response.status === 201) {                
                setAlertState({
                    open: true,
                    message: 'Order product mapping saved successfully!',
                    severity: 'success'
                });
                methods.reset(response.data);
            } else if (response.status === 400) {
                // Handle error response
                const errorMessages = response.response?.data?.errorMessages || {};
                
                // Handle nested error messages
                Object.entries(errorMessages).forEach(([key, value]) => {
                    // Parse the path for nested errors like "orderProducts[0].companyYarnOrderProduct.machine"
                    methods.setError(key, {
                        type: 'manual',
                        message: value
                    });
                });

                setAlertState({
                    open: true,
                    message: 'Failed to save order product mapping',
                    severity: 'error'
                });
            } else {
                setAlertState({
                    open: true,
                    message: response.response?.data?.errorMessages?.SystemError || 'An unexpected error occurred while saving the order',
                    severity: 'error'
                });
            }
        } catch (error) {
            console.error('Error saving order product mapping:', error);
            
            if (error.response?.status === 400) {
                const errorMessages = error.response?.data?.errorMessages || {};
                
                // Handle nested error messages
                Object.entries(errorMessages).forEach(([key, value]) => {
                    methods.setError(key, {
                        type: 'manual',
                        message: value
                    });
                });
                
                setAlertState({
                    open: true,
                    message: 'Please correct the errors in the form',
                    severity: 'error'
                });
            } else {
                setAlertState({
                    open: true,
                    message: 'An error occurred while saving the order',
                    severity: 'error'
                });
            }
        } finally {
            setIsSubmitting(false);
        }
    };

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
                    disabled={!selectedOrder || isSubmitting}
                >
                    {isSubmitting ? 'Saving...' : 'Save Order'}
                </Button>

                <Snackbar 
                    open={alertState.open}
                    autoHideDuration={6000}
                    onClose={handleAlertClose}
                    anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
                >
                    <Alert 
                        onClose={handleAlertClose} 
                        severity={alertState.severity}
                        variant="filled"
                    >
                        {alertState.message}
                    </Alert>
                </Snackbar>
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
    const [fabricDesignSelected, setFabricDesignSelected] = useState(false);

    const fetchYarnFabricDesignsByYarnAndDesignName = (yarnAndDesignName) => {
        fetchYarnFabricDesignByYarnsAndDesigns(yarnAndDesignName)
            .then(response => setYarnFabricDesignOptions(response.data))
            .catch(error => console.log('Error fetching yarns', error));
    }

    const fetchCompanyOrderProduct = (fabricDesign) => {
        if (fabricDesign?.id) {
            setFabricDesignSelected(true);
            fetchProductIdAndMachineByYarnFabricDesignId(fabricDesign.id)
                .then(response => setMachineOptions(response.data))
                .catch(error => {
                    alert('Error fetching machines selected fabric design');
                    console.log('Error fetching machines selected fabric design', error);
                });
        }
    }

    const fetchYarnFabricDesignMappingById = (id) => {
        fetchFabricDesignYarnMappingById(id)
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
                rules={{ 
                    required: "Machine no is required",
                    validate: {
                        validMachine: (value) => {
                            // Allow both machine object and manual input
                            if (!value) return "Machine no is required";
                            if (typeof value === 'string' && value.trim() === '') return "Machine no is required";
                            // Check if it's a manual input with machineNo
                            if (value.machineNo && typeof value.machineNo === 'string') return true;
                            // Check if it's a machine object
                            if (value.machine?.machineNo) return true;
                            return "Invalid machine format";
                        }
                    }
                }}
                render={({ field }) => (
                    <Autocomplete
            {...field}
            className='col-span-1 sm:col-span-2'
            size='small'
            freeSolo={machineOptions.length === 0}
            options={machineOptions}
            getOptionLabel={(option) => {
                if (typeof option === 'string') return option;
                if (option?.machineNo) return option.machineNo; // Handle manual input object
                return option.machine?.machineNo ? 
                    `${option.machine.machineNo} -${option.machine.dia}/${option.machine.guage}` : '';
            }}
            value={field.value}
            onChange={(_, newValue) => {
                if (machineOptions.length === 0) {
                    // Handle manual input when no options
                    const manualValue = typeof newValue === 'string' ? 
                        { machineNo: newValue } : newValue;
                    field.onChange(manualValue);
                    // Clear error if valid value entered
                    if (manualValue?.machineNo && manualValue.machineNo.trim() !== '') {
                        methods.clearErrors(`orderProducts.${index}.companyYarnOrderProduct.machine`);
                    }
                } else {
                    // Handle selection from options
                    if (newValue?.id) {
                        methods.setValue(`orderProducts.${index}.companyYarnOrderProduct.id`, newValue.id);
                    }
                    field.onChange(newValue?.machine || null);
                }
            }}
            onInputChange={(_, newInputValue) => {
                if (!newInputValue) {
                    methods.setValue(`orderProducts.${index}.companyYarnOrderProduct.machine`, null);
                    methods.setError(`orderProducts.${index}.companyYarnOrderProduct.machine`, {
                        type: 'required',
                        message: 'Machine no is required'
                    });
                } else if (machineOptions.length === 0) {
                    // Update value for manual input
                    field.onChange({ machineNo: newInputValue });
                }
            }}
            renderInput={(params) => (
                <TextField
                    {...params}
                    label="Machine"
                    variant="outlined"
                    error={!!methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.machine}
                    helperText={
                        methods.formState.errors.orderProducts?.[index]?.companyYarnOrderProduct?.machine?.message ||
                        (fabricDesignSelected && machineOptions.length === 0 && !field.value?.machineNo ? 
                            'No machines available, enter manually' : '')
                    }
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
                        selectedOrder={selectedOrder}
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
    methods,
    selectedOrder
}) => {
    const [open, setOpen] = useState(false);
    const [yarnOrderItems, setYarnOrderItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchYarnOrders();
    }, [selectedOrder?.id, rawMaterial?.yarn?.type]);

    const fetchYarnOrders = async () => {
        if (selectedOrder?.id && rawMaterial?.yarn?.type) {
            setLoading(true);
            setError(null);
            try {
                const response = await fetchYarnOrderItemsByOrderAndYarnType(
                    selectedOrder.company.id,
                    rawMaterial.yarn.id
                );
                setYarnOrderItems(response.data);
            } catch (err) {
                setError('Error fetching yarn orders');
                console.error('Error fetching yarn orders:', err);
            } finally {
                setLoading(false);
            }
        }
    };

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
            `orderProducts.${index}.rawMaterials.${rawIndex}.yarnOrderItems`, 
            selectedItems.map(item => ({ yarnOrderItem: item }))
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
                disabled={loading || !selectedOrder}
            >
                {loading ? 'Loading...' : 'Select Yarn Orders'}
            </Button>
            <Button
                variant="outlined"
                onClick={fetchYarnOrders}
                disabled={loading || !selectedOrder}
                startIcon={<Refresh />}
            >
                Refresh
            </Button>

            <Dialog 
                open={open}
                onClose={handleClose}
                maxWidth="md"
                fullWidth
            >
                <DialogTitle>
                    Select Yarn Orders
                    {loading && ' (Loading...)'}
                </DialogTitle>
                <DialogContent>
                    {error && (
                        <div className="text-red-500 mb-4">
                            {error}
                        </div>
                    )}
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
                                    <TableCell>Quantity Left</TableCell>
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
                                        <TableCell>{item.qtyAllocated}</TableCell>
                                        <TableCell>{item.qtyLeft}</TableCell>
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