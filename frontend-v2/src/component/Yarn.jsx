import React, { useEffect, useState } from "react";
import { addYarn, getYarn } from "../service/yarn";
import { useLocation } from "react-router-dom";
import { getCompanyList } from "../service/company";
import AsyncCreatableSelect from "react-select/async-creatable";


export const YarnForm = () => {
  const [yarn, setYarn] = useState({
    id: "",
    type: "",
    rate: "",
    company: "",
    description: "",
  });
  const [loading, setLoading] = useState(false)
  const [successMsg, setSuccessMsg] = useState(null);
  const [failureMsg, setFailureMsg] = useState(null);
  const [errors, setErrors] = useState({})
  const location = useLocation()
  const { yarnId } = location.state || {};
  const [selectedCompany, setSelectedCompany] = useState(null)
  const [isLoadingCompany, setIsLoadingCompany] = useState(false)
  const [companies, setCompanies] = useState([])

  useEffect(() => {
    console.log('yarnId: ', yarnId)
    if (yarnId) {
      fetchYarn(yarnId)
    }
  })
  
  const fetchYarn = async (id) => {
    const yarnListResponse = await getYarn(id);
    console.log('yarnList: id: ', yarnId, yarnListResponse.data)
    setYarn(yarnListResponse.data)
  }

  const loadCompanyOptions = async (inputValue) => {
    if (inputValue.trimStart().length < 3) {
      return []
    }
    setIsLoadingCompany(true)
    const response = await getCompanyList(inputValue);
    setIsLoadingCompany(false)
    return response.data.map((company) => ({
      label: `${company.name} - ${company.gst}`,
      value: company.id,
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (Object.keys(errors).length > 0) {
      setFailureMsg('Please check below errors')
      setTimeout(() =>{
        setFailureMsg(null)
      }, 5000)
      return;
    }

    setLoading(true)
    const trimmedInput = {
        ...yarn,
        type: yarn.type.trim(),
        company: selectedCompany?.name ? selectedCompany : null,
        description: yarn.description.trim(),
        rate: yarn.rate
    }

    const response = await addYarn(trimmedInput)
    if (response && response.status === 201) {
        setSuccessMsg(`Yarn Saved Successfully: ${response.data.type} [${response.data.id}]`)

        setYarn({
            id: null,
            type: "",
            company: "",
            description: "",
            rate: ""
        });
        const savedCompanyId = response.data.company?.id
        if (savedCompanyId) {
          setSelectedCompany({
            ...selectedCompany,
            id: savedCompanyId,
          })
        }

        setTimeout(() => {
            setSuccessMsg(null)
        }, 10000);
    } else {
        if (response && response.status === 400) {
          setFailureMsg('Please check below errors')
          setErrors(response.response.data.errorMessages)
          console.log('errors: ' , errors)
//           TODO: preSave key to be handled
        } else {
          setFailureMsg(`System error in saving yarn: ${response.code}`)
        }

        setTimeout(() => {
            setFailureMsg(null)
        }, 10000)
    }
    
    setLoading(false)
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setYarn({
      ...yarn,
      [name]: value.trimStart(),
    });
    setErrors((previousErrors) => {
      const updatedErrors = {...previousErrors};
      delete updatedErrors[name];
      return updatedErrors;
    })
  };

  const handleClickResponse = (e) => {
    setSuccessMsg(null)
    setFailureMsg(null)
  }

  const handlePriceChange = (e) => {
    const { name, value } = e.target;

    const regex = /^\d*\.?\d*$/;

    if (value === '' || regex.test(value)) {
      setYarn({
        ...yarn,
        [name]: value,
      });
      setErrors((previousErrors) => {
        const updatedErrors = {...previousErrors};
        delete updatedErrors[name];
        return updatedErrors;
      })
    }
  };

  const handleCompanyCreate = ( inputValue ) => {
    console.log('handleCompanyCreate: ', inputValue)
    const createdInput = inputValue.split('-');
    if (createdInput.length === 2) {
      setSelectedCompany({
        name: inputValue.split('-')[0].trim(),
        gst: inputValue.split('-')[1].trim(),
        label: `${inputValue.split('-')[0]} - ${inputValue.split('-')[1]}`
      })
      console.log('selectedCompany: ', selectedCompany)
      setErrors((prevErrors) => {
        const updatedErrors = {...prevErrors};
        delete updatedErrors['company'];
        return updatedErrors;
      })
    } else {
      setErrors({
        ...errors,
        company: 'Please create a valid company (eg. Company Name - GST )'
      })
    }
  }

  const setSelectedCompanyOption = (option) => {
    console.log('onChange: ', option)
    setSelectedCompany(option)
    setErrors((prevErrors) => {
      const updatedErrors = {...prevErrors};
      delete updatedErrors['company'];
      return updatedErrors;
    })
  }

  return (
      <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
            Yarn Quality
          </h2>
          <p className="mt-2 text-lg/8 text-gray-600">
            All yarn details should be added here
          </p>
        </div>
        <form onSubmit={handleSubmit} className="mx-auto mt-16 max-w-xl sm:mt-20">
          <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
            {successMsg || failureMsg ? (
              <div 
                className={`sm:col-span-2 top-full left-0 ${successMsg ? 'bg-green-100' : 'bg-red-100'} ${successMsg ? 'text-green-500' : 'text-red-500'} mt-1 rounded-md px-3 py-1`}
              >
                <span>{successMsg}{failureMsg}</span>
                <button
                  type="button"
                  className={`float-right ml-2 ${successMsg ? 'text-green-500' : 'text-red-500'} ${successMsg ? 'hover:text-green-700' : 'hover:text-red-700'}`}
                  onClick={handleClickResponse}
                >
                  <svg 
                    className="w-4 h-4" 
                    fill="none" 
                    stroke="currentColor" 
                    viewBox="0 0 24 24" 
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            ) : (
              ""
            )}
            <div className="sm:col-span-2">
              <label
                htmlFor="type"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Type / Quality
              </label>
              <div className="mt-2.5">
                <input
                  type="text"
                  name="type"
                  id="type"
                  autoComplete="yarnType"
                  className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.type ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                  required
                  value={yarn.type}
                  onChange={handleChange}
                />
                {errors.type ? <span className="text-red-500 text-sm mt-1 ml-1">{errors.type}</span> : ''}
              </div>
            </div>
            <div className="sm:col-span-2">
              <label
                htmlFor="company"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Company
              </label>
              <div className="mt-2.5">
                <AsyncCreatableSelect
                  cacheOptions
                  options={companies}
                  loadOptions={loadCompanyOptions}
                  isLoading={isLoadingCompany}
                  isClearable={companies ? true : false }
                  onChange={setSelectedCompanyOption}
                  onCreateOption={handleCompanyCreate}
                  value={selectedCompany}
                  placeholder="Please Select a company..."
                />
                {errors.company ? <span className="text-red-500 text-sm mt-1 ml-1">{errors.company}</span> : ''}
              </div>
            </div>
            <div className="sm:col-span-2">
              <label
                  htmlFor="rate"
                  className="block text-sm/6 font-semibold text-gray-900"
                >
                  Price
                </label>
                <div className="mt-2.5">
                  <div className="flex items-center rounded-md bg-white pl-3 outline outline-1 -outline-offset-1 outline-gray-300 has-[input:focus-within]:outline has-[input:focus-within]:outline-2 has-[input:focus-within]:-outline-offset-2 has-[input:focus-within]:outline-indigo-600">
                  <div className="shrink-0 select-none text-base text-gray-500 sm:text-sm/6">$</div>
                  <input
                    type="text"
                    name="rate"
                    id="rate"
                    placeholder="0.00"
                    className={`block min-w-0 grow py-1.5 pl-1 pr-3 text-base text-gray-900 placeholder:text-gray-400 focus:outline focus:outline-0 sm:text-sm/6" ${
                      errors.rate ? "outline-red-300" : "outline-gray-300"}`}
                    value={yarn.rate}
                    onChange={handlePriceChange}
                  />
                  <div className="grid shrink-0 grid-cols-1 focus-within:relative">
                    <select id="currency" name="currency" aria-label="Currency" className="col-start-1 row-start-1 w-full appearance-none rounded-md py-1.5 pl-3 pr-7 text-base text-gray-500 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                      <option value={1}>USD</option>
                    </select>
                    <svg className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4" viewBox="0 0 16 16" fill="currentColor" aria-hidden="true" data-slot="icon">
                      <path fillRule="evenodd" d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z" clipRule="evenodd" />
                    </svg>
                  </div>
                  </div>
                  {errors.rate ? (
                    <span className="text-red-500 text-sm mt-1 ml-1">
                      {errors.rate}
                    </span>
                  ) : (
                    ""
                  )}
                </div>
            </div>
            <div className="sm:col-span-2">
              <label
                htmlFor="description"
                className="block text-sm/6 font-semibold text-gray-900"
              >
                Description
              </label>
              <div className="mt-2.5">
                <textarea
                  name="description"
                  id="description"
                  rows="2"
                  className="block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600"
                  value={yarn.description}
                  onChange={handleChange}
                ></textarea>
              </div>
            </div>
          </div>
          <div className="mt-10">
            <button
              type="submit"
              disabled={loading}
              className="block w-full rounded-md bg-indigo-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              {loading ? "Adding Yarn..." : "Add Yarn"}
            </button>
          </div>
        </form>
      </div>
  );
};
