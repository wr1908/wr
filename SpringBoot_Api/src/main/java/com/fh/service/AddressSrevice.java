package com.fh.service;

import com.fh.model.Address;
import com.fh.model.DataTableResult;

public interface AddressSrevice {

    DataTableResult selectAddress(Address address);
}
