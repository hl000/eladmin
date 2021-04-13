<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">存货编码</label>
        <el-input v-model="query.fnumber" clearable placeholder="存货编码" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">状态（0可用，1不可用）</label>
        <el-input v-model="query.fstate" clearable placeholder="状态（0可用，1不可用）" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">关联部门</label>
        <el-input v-model="query.fdeptId" clearable placeholder="关联部门" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">是否必填（0：可选，1：必填）</label>
        <el-input v-model="query.frequired" clearable placeholder="是否必填（0：可选，1：必填）" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="fid">
            <el-input v-model="form.fid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="存货编码">
            <el-input v-model="form.fnumber" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="存货名称">
            <el-input v-model="form.fname" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="规格型号">
            <el-input v-model="form.fstd" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="单位">
            <el-input v-model="form.funit" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="归属部门">
            <el-input v-model="form.fdept" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="原始存货编码">
            <el-input v-model="form.foldNumber" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态（0可用，1不可用）">
            <el-input v-model="form.fstate" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.fremark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="关联部门">
            <el-input v-model="form.fdeptId" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="是否必填（0：可选，1：必填）">
            <el-input v-model="form.frequired" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="fid" label="fid" />
        <el-table-column prop="fnumber" label="存货编码" />
        <el-table-column prop="fname" label="存货名称" />
        <el-table-column prop="fstd" label="规格型号" />
        <el-table-column prop="funit" label="单位" />
        <el-table-column prop="fdept" label="归属部门" />
        <el-table-column prop="foldNumber" label="原始存货编码" />
        <el-table-column prop="fstate" label="状态（0可用，1不可用）" />
        <el-table-column prop="fremark" label="备注" />
        <el-table-column prop="fdeptId" label="关联部门" />
        <el-table-column prop="frequired" label="是否必填（0：可选，1：必填）" />
        <el-table-column v-permission="['admin','adminventory:edit','adminventory:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudAdminventory from '@/api/adminventory'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { fid: null, fnumber: null, fname: null, fstd: null, funit: null, fdept: null, foldNumber: null, fstate: null, fremark: null, fdeptId: null, frequired: null }
export default {
  name: 'Adminventory',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: 'getProduct', url: 'api/adminventory', idField: 'fid', sort: 'fid,desc', crudMethod: { ...crudAdminventory }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'adminventory:add'],
        edit: ['admin', 'adminventory:edit'],
        del: ['admin', 'adminventory:del']
      },
      rules: {
      },
      queryTypeOptions: [
        { key: 'fnumber', display_name: '存货编码' },
        { key: 'fstate', display_name: '状态（0可用，1不可用）' },
        { key: 'fdeptId', display_name: '关联部门' },
        { key: 'frequired', display_name: '是否必填（0：可选，1：必填）' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
