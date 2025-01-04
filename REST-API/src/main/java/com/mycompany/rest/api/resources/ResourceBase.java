/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rest.api.resources;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 *
 * @author akith
 */
public class ResourceBase {
    @Resource(lookup="jms/__defaultConnectionFactory")
    protected ConnectionFactory connFactory;
    
    @Resource(lookup="audioSystemTopic")
    protected Topic topic;
    
    @Resource(lookup="audioSystemQueue")
    protected Queue queue;
}
