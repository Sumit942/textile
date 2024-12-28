import React, { useState } from "react";

// Recursive Component
const NavbarItem = ({ item }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };
  
  return (
    <li className='relative group'>
      <a href={item.url}
        className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
        onMouseEnter={toggleMenu}
      >
        {item.name}
    </a>
    {item.children && item.children.length > 0 && (
    <div>
        <ul className={`absolute left-0 mt-2 w-48 bg-gray-800 rounded-md shadow-lg ${isOpen ? 'block' : 'hidden'}`}>
        {item.children.map((child) => (
            <NavbarItem key={child.id} item={child} />
        ))}
        </ul>
    </div>
    )}
    </li>
  );
};
export default NavbarItem;
