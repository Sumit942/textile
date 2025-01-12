import React, { useEffect, useState } from "react";
import { saveCompany } from "../service/company";
import { getStates } from "../service/stateApi";

const CompanyForm = () => {
  const [company, setCompany] = useState({
    name: "",
    address: {
      state: {
        id: 1, // Assuming a default state ID // Assuming a default state code
        name: '',
        code: '',
        country: {
          id: 1, // Assuming a default country ID // Assuming a default country name
        },
      },
      address: "",
      pinCode: "",
    },
    ofcAddress: null,
    gst: "",
    emailId: '',
    mobileNo: '',
    bankDetails: [],
    companyType: {
      id: 1, // Assuming a default company type ID
    },
  });
  const [states, setStates] = useState([])

  const [errors, setErrors] = useState({});
  const [successMsg, setSuccessMsg] = useState(null);
  const [failureMsg, setFailureMsg] = useState(null);
  const [loading, setLoading] = useState(false)


  useEffect(() => {
    fetchStates()
  
  }, [])

  const fetchStates = async () => {
    const response = await getStates();
    setStates(response)
  }
  


  const handleChange = (e) => {
    const { name, value } = e.target;
    setCompany({
      ...company,
      [name]: value.trimStart(),
    });
    setErrors({ ...errors, [name]: "" }); // Clear error for the field being edited
  };

  const handleAddressChange = (e) => {
    const { name, value } = e.target;
    setCompany({
      ...company,
      address: {
        ...company.address,
        [name]: value.trimStart(),
      },
    });
  };

  const handleStateChange = (e) => {
    const value = e.target.value;
    
    setCompany({
      ...company,
      address: {
        ...company.address,
        state: {
          ...company.address.state,
          id: value
        }
      }
    })
  }

  const handleBankDetailChange = (index, e) => {
    const { name, value } = e.target;
    const newBankDetails = [...company.bankDetails];
    newBankDetails[index][name] = value;
    setCompany({ ...company, bankDetails: newBankDetails });
  };

  const handleCompanyTypeChange = (e) => {
    const value = e.target.value;
    setCompany({
      ...company,
      companyType: {
        id: value
      }
    })
  }

  const handleClickResponse = (e) => {
    setSuccessMsg(null)
    setFailureMsg(null)
  }

  const addBankDetail = () => {
    setCompany({
      ...company,
      bankDetails: [
        ...company.bankDetails,
        { accountNo: "", bankName: "" , ifsc: "", branch: ""},
      ],
    });
  };

  const removeBankDetail = (index) => {
    const newBankDetails = company.bankDetails.filter((_, i) => i !== index);
    setCompany({ ...company, bankDetails: newBankDetails });
  };

  const validate = () => {
    const newErrors = {};
    if (!company.name) newErrors.name = "Company name is required";
    if (!company.address.address) newErrors.address = "Address is required";
    if (!company.address.state) newErrors.state = "State is required";
    if (!company.gst) newErrors.gst = "GST is required";
    // if (!/^[0-9]{2}[A-Z]{4}[0-9]{4}[A-Z]{1}[Z]{1}[0-9A-Z]{1}$/.test(company.gst)) {
    //     newErrors.gst = 'GST must be a valid 15 character GST number';
    // }
    if (!company.mobileNo) newErrors.mobileNo = "Mobile number is required";
    if (!/^\d{10}$/.test(company.mobileNo))
      newErrors.mobileNo = "Mobile number must be 10 digits";

    // Validate bank details
    company.bankDetails.forEach((bankDetail, index) => {
      if (!bankDetail.accountNo || bankDetail.accountNo.length < 10) {
        newErrors[`bankAccountNo${index}`] = "Account number is required";
      }
      if (!bankDetail.bankName) {
        newErrors[`bankName${index}`] = "Bank name is required";
      }
    });

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    // Prepare the data to be sent
    const dataToSend = {
      ...company,
      address: {
        ...company.address,
        fullAddress: `${company.address.address}, ${company.address.pinCode}`, // Construct full address
      },
    };

    console.log('submitting: ', dataToSend)
    // return;

    const response = await saveCompany(dataToSend);
    if (response && response.status === 201) {
      setSuccessMsg(`Company Saved ${response.data.name}:${response.data.id}`)
      setTimeout(() => {
        setSuccessMsg(null)
      }, 5000)
    } else {
      if (response && response.status === 400) {
        setFailureMsg(`Please check below validations`)
        //TODO: valication message
      } else {
        setFailureMsg(`System error in saving company: ${response.code}`)
      }
      setTimeout(() => {
        setFailureMsg(null)
      }, 10000)
    }
  };

  return (
    <div className="isolate bg-white px-6 py-5 sm:py-10 lg:px-8">
      <div className="mx-auto max-w-2xl text-center">
        <h2 className="text-balance text-4xl font-semibold tracking-tight text-gray-900 sm:text-5xl">
          Company Registration
        </h2>
      </div>
      <form onSubmit={handleSubmit} className="mx-auto mt-16 max-w-xl sm:mt-20">
        <div className="grid grid-cols-1 gap-x-8 gap-y-6 sm:grid-cols-2">
          {successMsg || failureMsg ? (
            <div
              className={`sm:col-span-2 top-full left-0 ${
                successMsg ? "bg-green-100" : "bg-red-100"
              } ${
                successMsg ? "text-green-500" : "text-red-500"
              } mt-1 rounded-md px-3 py-1`}
            >
              <span>
                {successMsg}
                {failureMsg}
              </span>
              <button
                type="button"
                className={`float-right ml-2 ${
                  successMsg ? "text-green-500" : "text-red-500"
                } ${
                  successMsg ? "hover:text-green-700" : "hover:text-red-700"
                }`}
                onClick={handleClickResponse}
              >
                <svg
                  className="w-4 h-4"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          ) : (
            ""
          )}
          <div className="sm:col-span-2">
            <label
              htmlFor="name"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              Company Name
            </label>
            <div className="mt-2.5">
              <input
                type="text"
                name="name"
                value={company.name}
                onChange={handleChange}
                required
                className={`w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.name ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />
              {errors.name && (<p className="text-red-500 text-xs">{errors.name}</p>)}
            </div>
          </div>
          <div className="sm:col-span-1">
            <label
              htmlFor="gst"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              GST
            </label>
            <div className="mt-2.5">
              <input
                type="text"
                name="gst"
                value={company.gst}
                onChange={handleChange}
                required
                className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.gst ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />
              {errors.gst && (
                <p className="text-red-500 text-xs">{errors.gst}</p>
              )}
            </div>
          </div>
          <div className="sm:col-span-1">
            <label
              htmlFor="state"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              State
            </label>
            <div className="mt-2.5">
              <select
                  id="state"
                  name="state"
                  defaultValue={company.address.state.id}
                  onChange={handleStateChange}
                  autoComplete="state"
                  className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pl-3 pr-8 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                >
                  <option key={0} value={0}>Select</option>
                  {
                    states.map(state => <option key={state.id} value={state.id}>{state.name}</option>)
                  }
                </select>
              {errors.gst && (
                <p className="text-red-500 text-xs">{errors.gst}</p>
              )}
            </div>
          </div>
          <div className="sm:col-span-2">
            <label
              htmlFor="address"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              Address
            </label>
            <div className="mt-2.5">
              <textarea
                name="address"
                id="address"
                rows="2"
                className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${
                  errors.address ? "outline-red-300" : "outline-gray-300"
                } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                value={company.address.address}
                onChange={handleAddressChange}
              ></textarea>
              {errors.address && (
                <p className="text-red-500 text-xs">{errors.address}</p>
              )}
            </div>
          </div>
          <div className="sm:col-span-1">
            <label className="block text-sm/6 font-semibold text-gray-900">
              Pin Code
            </label>
            <div className="mt-2.5">
              <input
                type="text"
                name="pinCode"
                value={company.address.pinCode}
                onChange={handleAddressChange}
                required
                className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.pinCode ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
              />
              {errors.pinCode && (
                <p className="text-red-500 text-xs">{errors.pinCode}</p>
              )}
            </div>
          </div>
          <div className="sm:col-span-1">
            <label
              htmlFor="country"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              Country
            </label>
            <div className="mt-2 grid grid-cols-1">
              <select
                id="country"
                name="country"
                defaultValue={1}
                autoComplete="country-name"
                className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pl-3 pr-8 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
              >
                <option value={1}>India</option>
              </select>
              <svg
                className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4"
                viewBox="0 0 16 16"
                fill="currentColor"
                aria-hidden="true"
                data-slot="icon"
              >
                <path
                  fillRule="evenodd"
                  d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z"
                  clipRule="evenodd"
                />
              </svg>
            </div>
          </div>
          <div className="sm:col-span-1">
            <label
              htmlFor="mobileNo"
              className="block text-sm/6 font-semibold text-gray-700"
            >
              Mobile Number
            </label>
            <input
              type="text"
              name="mobileNo"
              value={company.mobileNo}
              onChange={handleChange}
              required
              className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors.mobileNo ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
            />
            {errors.mobileNo && (<p className="text-red-500 text-xs">{errors.mobileNo}</p>)}
          </div>
          <div className="sm:col-span-1">
            <label
              htmlFor="companyType"
              className="block text-sm/6 font-semibold text-gray-900"
            >
              Company Type
            </label>
            <div className="mt-2 grid grid-cols-1">
              <select
                id="companyType"
                name="companyType"
                defaultValue={1}
                onChange={handleCompanyTypeChange}
                autoComplete="companyType"
                className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pl-3 pr-8 text-base text-gray-900 outline outline-1 -outline-offset-1 outline-gray-300 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
              >
                <option value={1}>Supplier</option>
                <option value={2}>Client</option>
                <option value={3}>Transport</option>
              </select>
              <svg
                className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4"
                viewBox="0 0 16 16"
                fill="currentColor"
                aria-hidden="true"
                data-slot="icon"
              >
                <path
                  fillRule="evenodd"
                  d="M4.22 6.22a.75.75 0 0 1 1.06 0L8 8.94l2.72-2.72a.75.75 0 1 1 1.06 1.06l-3.25 3.25a.75.75 0 0 1-1.06 0L4.22 7.28a.75.75 0 0 1 0-1.06Z"
                  clipRule="evenodd"
                />
              </svg>
            </div>
          </div>

            <div className="sm:col-span-2">
                {company.bankDetails && company.bankDetails.length ? <h3 className="text-lg font-semibold mb-2">Bank Details</h3> : ''}
                {
                    company.bankDetails && company.bankDetails.length > 0
                    ? company.bankDetails.map((bankDetail, index) => (
                        <BankDetail
                          key={index}
                          bankDetail={bankDetail}
                          index={index}
                          errors={errors}
                          handleBankDetailChange={handleBankDetailChange}
                          removeBankDetail={removeBankDetail}
                          />
                    ))
                    : ""
                }
            </div>
        </div>
        <div className="mt-2.5">
            <button
                type="button"
                onClick={addBankDetail}
                className="mb-3 w-full rounded-md bg-gray-50 px-3.5 py-3.5 text-center text-sm font-semibold text-indigo-500 shadow-sm hover:bg-gray-200 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600"
            >
            Add Bank Detail
            </button>
            <button type="submit" 
              disabled={loading}
              className="block w-full rounded-md bg-indigo-600 px-3.5 py-2.5 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
            {loading ? "Submitting..." : "Submit"}
        </button>
        </div>
      </form>
    </div>
  );
};

const BankDetail = ({bankDetail, index, errors, handleBankDetailChange, removeBankDetail}) => {
    return (
        <div key={index} className="mb-4 border p-4 rounded-md">
            <div className="mb-2">
                <label className="block text-sm/6 font-semibold text-gray-700">Account Number</label>
                <input
                    type="text"
                    name="accountNo"
                    value={bankDetail.accountNo}
                    onChange={(e) => handleBankDetailChange(index, e)}
                    required
                    className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors[`bankAccountNo${index}`] ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                />
                {errors[`bankAccountNo${index}`] && <p className="text-red-500 text-xs">{errors[`bankAccountNo${index}`]}</p>}
            </div>
            <div className="mb-2">
                <label className="block text-sm/6 font-semibold text-gray-700">Bank Name</label>
                <input
                    type="text"
                    name="bankName"
                    value={bankDetail.bankName}
                    onChange={(e) => handleBankDetailChange(index, e)}
                    required
                    className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors[`bankName${index}`] ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                />
                {errors[`bankName${index}`] && <p className="text-red-500 text-xs">{errors[`bankName${index}`]}</p>}
            </div>
            <div className="mb-2">
                <label className="block text-sm/6 font-semibold text-gray-700">IFSC</label>
                <input
                    type="text"
                    name="ifsc"
                    value={bankDetail.ifsc}
                    onChange={(e) => handleBankDetailChange(index, e)}
                    required
                    className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors[`ifsc${index}`] ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                />
                {errors[`ifsc${index}`] && <p className="text-red-500 text-xs">{errors[`ifsc${index}`]}</p>}
            </div>
            <div className="mb-2">
                <label className="block text-sm/6 font-semibold text-gray-700">Branch</label>
                <input
                    type="text"
                    name="branch"
                    value={bankDetail.branch}
                    onChange={(e) => handleBankDetailChange(index, e)}
                    required
                    className={`block w-full rounded-md bg-white px-3.5 py-2 text-base text-gray-900 outline outline-1 -outline-offset-1 ${ errors[`branch${index}`] ? 'outline-red-300' : 'outline-gray-300' } placeholder:text-gray-400 focus:outline focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600`}
                />
                {errors[`branch${index}`] && <p className="text-red-500 text-xs">{errors[`branch${index}`]}</p>}
            </div>
            <button type="button" onClick={() => removeBankDetail(index)} className="text-red-500 hover:underline">Remove Bank Detail</button>
        </div>
    )
}

export default CompanyForm;
