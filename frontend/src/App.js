import React, { useState, useEffect } from "react";
import { setupCSRF } from "./services/api";
import { deleteState, getStates, saveState } from "./services/StateApi";


function App() {
    const [states, setStates] = useState([]);
    const [name, setName] = useState('');
    const [code, setCode] = useState('');
    const [country, setCountry] = useState('');

    useEffect(() => {
        setupCSRF();
        fetchStates();
    }, [])
    
    const fetchStates = async () => {
        const data = await getStates();
        setStates(data);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newState = {
          name: name,
          code: code,
          country: { id: country },
        };
        console.log('saving data',newState)
        await saveState(newState);
        setName('');
        setCode('');
        setCountry('');
        fetchStates()
    }

    const handleDelete = async (id) => {
        await deleteState(id);
        const updatedStates = states.filter(state => state.id !== id)
        setStates(updatedStates)
    }

    return (
        <div>
            <h1>State Management</h1>
            <form onSubmit={handleSubmit}>
                <input 
                    type="text"
                    placeholder="name"
                    name="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input 
                    type="text"
                    placeholder="code"
                    name="code"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                />
                <input 
                    type="text"
                    placeholder="country"
                    name="country"
                    value={country}
                    onChange={(e) => setCountry(e.target.value)}
                />
                <button type="submit">Add State</button>
            </form>

            <h2>State List</h2>
            <ul>
                {states.map((state) => (
                    <li key={state.id}>
                        {state.code} - {state.name} - {state.country.id}
                        <button onClick={() => handleDelete(state.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    )

}

export default App;
