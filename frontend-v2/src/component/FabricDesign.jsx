import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { TextField, Button, Container, Typography, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper, } from '@mui/material';
import { deleteFabricDesign, fetchAllFabricDesigns, fetchFabricDesignById, saveFabricDesign } from '../service/fabricDesign';

const FabricDesign = ({ id }) => {
    const { register, handleSubmit, setValue, reset, formState: { errors, isSubmitting } } = useForm();
    const [fabricDesigns, setFabricDesigns] = useState([]);
    const [buttonText, setButtonText] = useState('View List');
    const [submitButtonText, setSubmitButtonText] = useState('Submit');

    const handleButtonClick = () => {
        loadFabricDesigns();
        setButtonText('Refresh List');
    };

    useEffect(() => {
        if (id) {
            fetchFabricDesignById(id)
                .then(response => {
                    const { design, name } = response.data;
                    setValue('id', id);
                    setValue('name', name);
                    setValue('description', design);
                    setSubmitButtonText('Update');
                })
                .catch(error => {
                    console.error('Error fetching fabric design:', error);
                    errors.design = error?.response?.data?.message || 'Error fetching fabric design';
                });
        }
    }, [id]);

    useEffect(() => {
        loadFabricDesigns();
    }, []);

    const loadFabricDesigns = () => {
        fetchAllFabricDesigns()
            .then(response => {
                setFabricDesigns(response.data);
            })
            .catch(error => {
                console.error('Error fetching fabric designs:', error);
            });
    };

    const onSubmit = (data) => {
        saveFabricDesign(data)
            .then(response => {
                console.log('Fabric Design saved successfully:', response);
                alert('Fabric Design saved successfully');
                reset();
                setSubmitButtonText('Submit');
            })
            .catch(error => {
                console.error('Error saving fabric design:', error);
                alert('Error saving fabric design');
            });
    };

    const [searchTerm, setSearchTerm] = useState('');

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const filteredFabricDesigns = fabricDesigns.filter((design) =>
        design.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        design.description.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleEdit = (design) => {
        setValue('id', design.id);
        setValue('name', design.name);
        setValue('description', design.description);
        setSubmitButtonText('Update');
    };

    const handleDelete = (id) => {
        // Add your delete logic here
        console.log('Delete fabric design with id:', id);
        // After deleting, refresh the list
        deleteFabricDesign(id)
            .then(response => {
                console.log('Fabric Design deleted successfully:', response);
                alert('Fabric Design deleted successfully');
                setFabricDesigns(fabricDesigns.filter(design => design.id !== id));
            })
            .catch(error => {
                console.error('Error deleting fabric design:', error);
                alert('Error deleting fabric design');
            });
    };

    return (
        <Container maxWidth="sm">
            <Typography variant="h4" component="h1" gutterBottom>
                Fabric Design Form
            </Typography>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input type="hidden" {...register('id')} />

                <TextField
                    label="Name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    {...register('name', { required: 'Name is required' })}
                    error={!!errors.name}
                    helperText={errors.name ? errors.name.message : ''}
                />

                <TextField
                    label="Description"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    {...register('description', { required: 'Design is required' })}
                    error={!!errors.description}
                    helperText={errors.description ? errors.description.message : ''}
                />
                
                <Button type="submit" variant="contained" color="primary" disabled={isSubmitting}>
                    {isSubmitting ? 'Submitting...' : submitButtonText}
                </Button>
                <Button variant="contained" color="secondary" onClick={handleButtonClick}>
                    {buttonText}
                </Button>
            </form>

            <Typography variant="h5" component="h2" gutterBottom>
                Fabric Designs List
            </Typography>
            <TextField
                label="Search"
                variant="outlined"
                fullWidth
                margin="normal"
                value={searchTerm}
                onChange={handleSearchChange}
            />
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Description</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredFabricDesigns.map((design) => (
                            <TableRow key={design.id}>
                                <TableCell>{design.name}</TableCell>
                                <TableCell>{design.description}</TableCell>
                                <TableCell>
                                    <Button variant="contained" color="primary" onClick={() => handleEdit(design)}>
                                        Edit
                                    </Button>
                                    <Button variant="contained" color="secondary" onClick={() => handleDelete(design.id)}>
                                        Delete
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </Container>
    );
};

export default FabricDesign;