import React, { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { Button, TextField, Autocomplete, Snackbar, Alert } from '@mui/material';
import { saveYarnBuilty } from '../service/yarnBuilty';
import { fetchByYarnInvoiceNo } from '../service/companyYarnOrder';
import { getCompanyList } from '../service/company';

const YarnBuiltyForm = () => {
    const methods = useForm({
        defaultValues: {
            companyYarnOrder: null,
            receivedDt: '',
            loadUnloadCharges: '',
            boxes: '',
            tranportCompany: null,
            vehicleNo: '',
            quantity: ''
        }
    });
    const { control, register, handleSubmit, setValue, formState: { errors } } = methods;

    const [orderInvoiceInputValue, setOrderInvoiceInputValue] = useState('');
    const [transportCompanyOptions, setTransportCompanyOptions] = useState([]);
    const [open, setOpen] = useState(false);
    const [alertSeverity, setAlertSeverity] = useState('success');
    const [alertMessage, setAlertMessage] = useState('');

    const handleClose = () => {
        setOpen(false);
    };

    const handleInvoiceSearch = async () => {
        if (!orderInvoiceInputValue) return;
        try {
            const response = await fetchByYarnInvoiceNo(orderInvoiceInputValue);
            if (response.status === 200) {
                setValue('companyYarnOrder', response.data);
                setAlertSeverity('success');
                setAlertMessage('Order found!');
                setOpen(true);
            } else {
                setAlertSeverity('error');
                setAlertMessage('Order not found with this Invoice No');
                setOpen(true);
            }
        } catch (error) {
            console.error('Error fetching order:', error);
            setAlertSeverity('error');
            setAlertMessage('Error fetching order');
            setOpen(true);
        }
    };

    const fetchTransportCompanies = async (name) => {
        try {
            const response = await getCompanyList(name);
            setTransportCompanyOptions(response.data);
        } catch (error) {
            console.error('Error fetching companies:', error);
        }
    };

    const onSubmit = async (data) => {
        const payload = {
            ...data,
            companyYarnOrder: data.companyYarnOrder?.id ? { id: data.companyYarnOrder.id } : null,
            tranportCompany: data.tranportCompany?.id ? { id: data.tranportCompany.id } : null
        };

        const response = await saveYarnBuilty(payload);
        if (response.status === 201) {
            setAlertSeverity('success');
            setAlertMessage('Yarn Builty saved successfully!');
            setOpen(true);
            methods.reset();
        } else {
            setAlertSeverity('error');
            setAlertMessage('Error occurred while saving Yarn Builty!');
            setOpen(true);
        }
    };

    return (
        <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
            <div className="mx-auto max-w-2xl text-center">
                <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
                    Save Yarn Builty
                </h2>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="mx-auto mt-16 max-w-xl sm:mt-20 space-y-4">
                <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
                    <div className="sm:col-span-2 flex gap-2">
                        <TextField
                            fullWidth
                            label="Search Company Yarn Order (Invoice No)"
                            variant="outlined"
                            value={orderInvoiceInputValue}
                            onChange={(e) => setOrderInvoiceInputValue(e.target.value)}
                        />
                        <Button variant="contained" onClick={handleInvoiceSearch}>Search</Button>
                    </div>

                    <div className="sm:col-span-2">
                        <TextField
                            fullWidth
                            label="Selected Order ID"
                            variant="outlined"
                            disabled
                            value={methods.watch('companyYarnOrder')?.id || ''}
                        />
                        {/* Hidden field to register companyYarnOrder */}
                        <Controller
                            name="companyYarnOrder"
                            control={control}
                            render={() => null}
                        />
                    </div>

                    <TextField
                        {...register('receivedDt', { required: "Received Date is required" })}
                        label="Received Date"
                        variant="outlined"
                        type='date'
                        slotProps={{ inputLabel: { shrink: true } }}
                        error={!!errors.receivedDt}
                        helperText={errors.receivedDt?.message}
                    />

                    <TextField
                        {...register('quantity', { required: "Quantity is required" })}
                        label="Quantity"
                        variant="outlined"
                        type="number"
                        error={!!errors.quantity}
                        helperText={errors.quantity?.message}
                    />

                    <TextField
                        {...register('boxes', { required: "Boxes is required" })}
                        label="Boxes"
                        variant="outlined"
                        type="number"
                        error={!!errors.boxes}
                        helperText={errors.boxes?.message}
                    />

                    <TextField
                        {...register('loadUnloadCharges')}
                        label="Load/Unload Charges"
                        variant="outlined"
                        type="number"
                    />

                    <Controller
                        control={control}
                        name='tranportCompany'
                        render={({ field }) => (
                            <Autocomplete
                                {...field}
                                options={transportCompanyOptions}
                                getOptionLabel={(option) => option.name || ''}
                                onInputChange={(_, newInputValue) => {
                                    if (newInputValue.trim() !== '') {
                                        fetchTransportCompanies(newInputValue);
                                    }
                                }}
                                onChange={(_, newValue) => field.onChange(newValue)}
                                renderInput={(params) => (
                                    <TextField
                                        {...params}
                                        label="Transport Company"
                                        variant="outlined"
                                        error={!!errors.tranportCompany}
                                        helperText={errors.tranportCompany?.message}
                                    />
                                )}
                            />
                        )}
                    />

                    <TextField
                        {...register('vehicleNo')}
                        label="Vehicle No"
                        variant="outlined"
                    />
                </div>

                <Button fullWidth type="submit" variant="contained" color="secondary" sx={{ mt: 3 }}>
                    Save Yarn Builty
                </Button>

                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert onClose={handleClose} severity={alertSeverity} variant="filled" sx={{ width: '100%' }}>
                        {alertMessage}
                    </Alert>
                </Snackbar>
            </form>
        </div>
    );
};

export default YarnBuiltyForm;
