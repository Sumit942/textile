import React, { useEffect, useState } from 'react';
import { fetchYarnFabricDesignByYarnsAndDesigns } from '../service/yarnFabricDesign';
import { fetchProductIdAndMachineByYarnFabricDesignId } from '../service/companyYarnOrderProduct';

const CompanyOrderProduct = () => {
    const [yarnFabricDesignOptions, setYarnFabricDesignOptions] = useState([]);
    const [machineOptions, setMachineOptions] = useState([])

    const fetchYarnFabricDesignsByYarnAndDesignName = (yarnAndDesignName) => {
        fetchYarnFabricDesignByYarnsAndDesigns(yarnAndDesignName)
            .then(response => setYarnFabricDesignOptions(response.data))
            .catch(error => {
                console.log('Error fetching yarns', error);
            })
    }

    const fetchCompanyOrderProduct = (fabricDesign) => {
        if (fabricDesign?.id) {
            fetchProductIdAndMachineByYarnFabricDesignId(fabricDesign.id)
                .then(response => {
                    console.log('machineOpt: ', response.data)
                    setMachineOptions(response.data)
                })
                .catch(error => {
                    alert('Error fetching machines selected fabric design')
                    console.log('Error fetching machines selected fabric design', error)
                })
        }
    }

    return (
        <div className='col-span-2 sm:col-span-4 mb-2 border p-4 rounded-md grid grid-cols-2 gap-x-6 gap-y-4 sm:grid-cols-4'>
            <Controller
                control={control}
                name={`companyYarnOrderProductMappings.${index}.companyYarnOrderProduct.yarnFabricDesign`}
                rules={{
                    required: "Yarn Fabric Design is required",
                }}
                defaultValue={null}
                render={({ field }) => (
                    <Autocomplete
                        className='col-span-3'
                        size='small'
                        freeSolo
                        options={yarnFabricDesignOptions}
                        getOptionLabel={(option) => option.qualityName || ''}
                        onChange={(_, newValue) => {
                            field.onChange(newValue);
                            fetchCompanyOrderProduct(newValue)
                        }}
                        onInputChange={(_, newInputValue) => {
                            if (newInputValue.trim() !== '') {
                                fetchYarnFabricDesignsByYarnAndDesignName(newInputValue);
                            }
                        }}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                label="Fabric Design"
                                variant="outlined"
                                error={!!methods.formState.errors.companyYarnOrderProductMappings?.[index]?.companyYarnOrderProduct?.yarnFabricDesign}
                                helperText={methods.formState.errors.companyYarnOrderProductMappings?.[index]?.companyYarnOrderProduct?.yarnFabricDesign?.message || ''}
                            />
                        )}
                    />
                )}
            />
            <Controller
                name={`companyYarnOrderProductMappings.${index}.companyYarnOrderProduct.machine`}
                control={control}
                defaultValue={null}
                rules={{
                    required: "Machine no is required",
                }}
                render={({ field }) => (
                    <Autocomplete
                        {...field}
                        className='col-span-1'
                        size='small'
                        freeSolo
                        options={machineOptions}
                        getOptionLabel={(option) => option.machine?.machineNo ? (`${option.machine.machineNo}  -${option.machine.dia}/${option.machine.guage}`) : ''}
                        openOnFocus
                        value={field.value?.machine || null}
                        onChange={(_, newValue) => {
                            console.log('onCHnage machine: ', newValue)
                            if (newValue?.id) {
                                methods.setValue(`companyYarnOrderProductMappings.${index}.companyYarnOrderProduct.id`, newValue.id)
                            }
                            if (newValue?.machine) {
                                field.onChange(newValue.machine || null);
                            }
                        }}
                        onInputChange={(_, newInputValue) => {
                            methods.setValue(`companyYarnOrderProductMappings.${index}.companyYarnOrderProduct.machine`, null)
                        }}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                label="Machine"
                                variant="outlined"
                                error={!!methods.formState.errors.companyYarnOrderProductMappings?.[index]?.companyYarnOrderProduct?.machine}
                                helperText={methods.formState.errors.companyYarnOrderProductMappings?.[index]?.companyYarnOrderProduct?.machine?.message}
                            />
                        )}
                    />
                )}
            />
            <Controller
                name={`companyYarnOrderProductMappings.${index}.quantity`}
                control={control}
                defaultValue=''
                rules={{ required: 'Quantity is required', pattern: { value: /^[0-9]+$/, message: "Quantity must be a number" } }}
                render={({ field, fieldState }) => (
                    <TextField
                        {...field}
                        label="Quantity"
                        variant="outlined"
                        size="small"
                        className="col-span-1"
                        error={!!fieldState.error}
                        helperText={fieldState.error?.message}
                    />
                )}
            />
            <Button size='small' className='col-span-2' variant="contained" color="error" onClick={() => removeOrderProduct(index)}
                startIcon={<Remove />}
            >
                Remove product
            </Button>
        </div>
    )
}

export default CompanyOrderProduct;