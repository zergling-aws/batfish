parser grammar A10_interface;

import A10_common;

options {
    tokenVocab = A10Lexer;
}

s_interface: INTERFACE si_definition;

si_definition: sid_ethernet | sid_loopback | sid_ve;

sid_ethernet: ETHERNET num = ethernet_number NEWLINE sid*;

sid_loopback: LOOPBACK num = loopback_number NEWLINE sid*;

sid_ve: VE num = vlan_number NEWLINE sid*;

sid: (sid_disable | sid_enable | sid_ip | sid_mtu | sid_name);

sid_enable: ENABLE NEWLINE;

sid_disable: DISABLE NEWLINE;

sid_ip: IP sidi_address;

sidi_address: ADDRESS ip_prefix NEWLINE;

sid_mtu: MTU interface_mtu NEWLINE;

sid_name: NAME interface_name_str NEWLINE;

// 434-1500
interface_mtu: uint16;