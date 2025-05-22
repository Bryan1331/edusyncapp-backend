import React, { useState, useEffect } from 'react';
import { Button, Table, Modal, Form, Input, InputNumber, Select } from 'antd';
import axios from 'axios';

const ClassroomList = () => {
  const [classrooms, setClassrooms] = useState([]);
  const [courses, setCourses] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [selectedClassroom, setSelectedClassroom] = useState(null);

  useEffect(() => {
    fetchClassrooms();
    fetchCourses();
  }, []);

  const fetchClassrooms = async () => {
    try {
      const response = await axios.get('/api/classrooms');
      setClassrooms(response.data);
    } catch (error) {
      console.error('Error fetching classrooms:', error);
    }
  };

  const fetchCourses = async () => {
    try {
      const response = await axios.get('/api/courses');
      setCourses(response.data);
    } catch (error) {
      console.error('Error fetching courses:', error);
    }
  };

  const handleSubmit = async (values) => {
    try {
      if (selectedClassroom) {
        await axios.put(`/api/classrooms/${selectedClassroom.id}`, values);
      } else {
        await axios.post('/api/classrooms', values);
      }
      setModalVisible(false);
      form.resetFields();
      fetchClassrooms();
    } catch (error) {
      console.error('Error saving classroom:', error);
    }
  };

  const showDeleteConfirm = (classroom) => {
    setSelectedClassroom(classroom);
    setDeleteModalVisible(true);
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`/api/classrooms/${selectedClassroom.id}`);
      setDeleteModalVisible(false);
      fetchClassrooms();
    } catch (error) {
      console.error('Error deleting classroom:', error);
    }
  };

  const columns = [
    {
      title: 'Nombre',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Capacidad',
      dataIndex: 'capacity',
      key: 'capacity',
    },
    {
      title: 'Ubicación',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: 'Curso',
      dataIndex: 'course',
      key: 'course',
      render: (course) => course?.name,
    },
    {
      title: 'Acciones',
      key: 'actions',
      render: (_, record) => (
        <>
          <Button type="primary" onClick={() => {
            setSelectedClassroom(record);
            form.setFieldsValue(record);
            setModalVisible(true);
          }}>
            Editar
          </Button>
          <Button type="danger" onClick={() => showDeleteConfirm(record)} style={{ marginLeft: '8px' }}>
            Eliminar
          </Button>
        </>
      ),
    },
  ];

  return (
    <div>
      <Button type="primary" onClick={() => {
        setSelectedClassroom(null);
        form.resetFields();
        setModalVisible(true);
      }} style={{ marginBottom: '16px' }}>
        Nuevo Aula
      </Button>

      <Table dataSource={classrooms} columns={columns} rowKey="id" />

      <Modal
        title={selectedClassroom ? "Editar Aula" : "Nueva Aula"}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <Form
          form={form}
          onFinish={handleSubmit}
          layout="vertical"
        >
          <Form.Item
            name="name"
            label="Nombre"
            rules={[
              { required: true, message: 'El nombre es obligatorio' },
              { min: 3, message: 'El nombre debe tener al menos 3 caracteres' },
              { max: 50, message: 'El nombre no puede exceder 50 caracteres' },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="capacity"
            label="Capacidad"
            rules={[
              { required: true, message: 'La capacidad es obligatoria' },
              { type: 'number', min: 1, message: 'La capacidad debe ser al menos 1' },
              { type: 'number', max: 100, message: 'La capacidad no puede exceder 100' },
            ]}
          >
            <InputNumber />
          </Form.Item>

          <Form.Item
            name="location"
            label="Ubicación"
            rules={[{ required: true, message: 'La ubicación es obligatoria' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="courseId"
            label="Curso"
            rules={[{ required: true, message: 'El curso es obligatorio' }]}
          >
            <Select>
              {courses.map(course => (
                <Select.Option key={course.id} value={course.id}>
                  {course.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit">
              {selectedClassroom ? "Actualizar" : "Crear"}
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Confirmar eliminación"
        open={deleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setDeleteModalVisible(false)}
      >
        <p>¿Está seguro que desea eliminar esta aula?</p>
      </Modal>
    </div>
  );
};

export default ClassroomList;